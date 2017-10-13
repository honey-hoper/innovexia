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
    fun startPresentationActivity()
    fun startCreateVisitActivity()
    fun startSplashActivity()
}