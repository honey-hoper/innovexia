package com.webhopers.innovexia.activities.mainActivity

import android.content.Context
import android.content.SharedPreferences
import com.webhopers.innovexia.models.ProductCategory
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(val view: MainView) {


    /**
     *
     * it fetches the product categories from woocommerce
     * and passes it to filter function
     */
    fun getCategories() {
        view.showProgressBar(true)
        WooCommerceClient.get()
                .create(WooCommerce::class.java)
                .categories()
                .enqueue(object : Callback<List<ProductCategory>> {
                    override fun onFailure(call: Call<List<ProductCategory>>, t: Throwable) {
                        view.showProgressBar(false)
                        view.makeToast("Could not fetch categories: ${t.message}")
                    }

                    override fun onResponse(call: Call<List<ProductCategory>>, response: Response<List<ProductCategory>>) {
                        view.showProgressBar(false)
                        if (response.isSuccessful) {
                            view.startPresentationActivity(response.body()!!)
                        } else {
                            view.makeToast("Could not fetch categories: ${response.code()}")
                        }
                    }
                })
    }

    /**
     *
     * log customer out
     */
    fun LogOut() {
        val preferences = view.getSharedPreferences(Constants.customerStatusFile)
        SharedPreferenceService.setCustomerStatus(preferences, Constants.customerLoggedOut)
        RealmDatabaseService.removeCustomer()
        view.startSplashActivity()
    }
}

interface MainView {
    fun showProgressBar(bool: Boolean)
    fun makeToast(message: String)
    fun getSharedPreferences(fileName: String): SharedPreferences
    fun startPresentationActivity(list: List<ProductCategory>)
    fun startCreateVisitActivity()
    fun startSplashActivity()
}