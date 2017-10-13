package com.webhopers.innovexia.services

import android.content.Context
import com.webhopers.innovexia.models.Product
import com.webhopers.innovexia.utils.convertToProductRealm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Syncher {

    companion object {

        val woocomm by lazy { WooCommerceClient.get().create(WooCommerce::class.java) }

        fun initiate() {
            syncProducts()
        }

        //syncs products and saves to database
        private fun syncProducts() {

            val products = mutableListOf<Product>()
            var total = 0
            var remaining = 0
            var offset = 0
            var perPage = 100

            //fetching single product to check total product count
            //from X-WP-Total header
            woocomm.products(perPage = "1")
                    .enqueue(object : Callback<List<Product>> {
                        override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                            if (response.isSuccessful) {
                                total = response.headers().get("X-WP-Total")!!.toInt()
                                remaining = total

                                //fethcing all products on a new thread
                                Thread {
                                    while (remaining > 0) {
                                        if (remaining <= 100) {
                                            perPage = remaining
                                            remaining = 0
                                        }
                                        products.addAll(getProducts(perPage.toString(), offset.toString()))
                                        remaining -= 100
                                        offset += 100
                                    }

                                //save products to database
                                    RealmDatabaseService.saveProducts(products)

                                    RealmDatabaseService.getProducts().forEach {
                                        println(it.name)
                                    }

                                }.start()
                            }
                        }
                        override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                    })

            }

        //synchronous method to fetch products
        fun getProducts(perPage: String, offset: String) : List<Product> {
            return woocomm.products(perPage = perPage, offset = offset)
                    .execute()
                    .body()!!
        }

    }
}