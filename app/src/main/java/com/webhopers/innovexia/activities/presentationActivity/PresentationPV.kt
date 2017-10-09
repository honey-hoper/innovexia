package com.webhopers.innovexia.activities.presentationActivity

import com.webhopers.innovexia.models.Product
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PresentationPresenter(val view: PresentationView) {

    /**
     *
     * fetches products of specified category
     * from woocommerce
     */
    fun getProducts(id: String) {
        view.showProgressBar(true)
        WooCommerceClient.get().create(WooCommerce::class.java)
                .productsByCategories(id)
                .enqueue(object : Callback<List<Product>> {
                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                        view.showProgressBar(false)
                        view.makeToast("${t.message}")
                    }

                    override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                        view.showProgressBar(false)
                        if (response.isSuccessful) filterUrlsAndSetAdapter(response.body()!!)
                        else view.makeToast(response.code().toString())
                    }
                })
    }

    /**
     *
     * filters out the urls from products
     * and sets the recycler view adapter
     */
    fun filterUrlsAndSetAdapter(list: List<Product>) {
        val filteredList = list.filter { it.publish }
        val urls = filteredList.map { it.label }
        view.setAdapter(urls)
    }
}

interface PresentationView {
    fun makeToast(message: String)
    fun showProgressBar(bool: Boolean)
    fun setAdapter(list: List<String?>)
}