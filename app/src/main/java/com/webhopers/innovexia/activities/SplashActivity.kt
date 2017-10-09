package com.webhopers.innovexia.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.loginActivity.LoginActivity
import com.webhopers.innovexia.activities.presentationActivity.PresentationActivity
import com.webhopers.innovexia.models.ProductCategory
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.Constants
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferences = getSharedPreferences(Constants.customerStatusFile, Context.MODE_PRIVATE)
        val customerStatus = SharedPreferenceService.getCustomerStatus(preferences)

        //checking customer status whether logged in or not
        if (customerStatus == Constants.customerLoggedIn)
            startMainActivity()
        else if (customerStatus == Constants.customerLoggedOut)
            startLoginActivity()
    }

    /**
     *
     * ui functions
     */
    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}
