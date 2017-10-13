package com.webhopers.innovexia.services

import com.webhopers.innovexia.models.Product
import com.webhopers.innovexia.models.ProductCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Syncher {

    companion object {

        val woocomm by lazy { WooCommerceClient.get().create(WooCommerce::class.java) }

        fun initiate(syncherInterface: SyncherInterface) {
            syncherInterface.preSync()
            syncProducts()
            syncCategories(syncherInterface)
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

        //synchronous method to fetch products, gets called in syncProducts
        fun getProducts(perPage: String, offset: String) : List<Product> {
            return woocomm.products(perPage = perPage, offset = offset)
                    .execute()
                    .body()!!
        }

        //fetches product categories and stores in database
        private fun syncCategories(syncherInterface: SyncherInterface) {
            woocomm.categories()
                    .enqueue(object : Callback<List<ProductCategory>> {
                        override fun onFailure(call: Call<List<ProductCategory>>, t: Throwable) {
                            syncherInterface.postSync("Failed due to ${t.message}")
                        }

                        override fun onResponse(call: Call<List<ProductCategory>>, response: Response<List<ProductCategory>>) {
                            if (response.isSuccessful) {
                                RealmDatabaseService.saveCategories(response.body()!!)
                                syncherInterface.postSync("Up to date")
                            } else {
                                syncherInterface.postSync("Failed due to ${response.code()}")
                            }
                        }

                    })
        }

    }

    //interface for activities to communicate with syncher
    interface SyncherInterface {
        fun preSync()
        fun postSync(message: String)
    }
}