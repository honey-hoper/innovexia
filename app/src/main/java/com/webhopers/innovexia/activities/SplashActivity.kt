package com.webhopers.innovexia.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.loginActivity.LoginActivity
import com.webhopers.innovexia.activities.mainActivity.MainActivity
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.utils.Constants

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
