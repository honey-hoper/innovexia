package com.webhopers.innovexia.services

import android.content.SharedPreferences
import com.webhopers.innovexia.utils.Constants

class SharedPreferenceService {

    companion object {

        val CUSTOMER_STATUS = "CUSTOMER_STATUS"

        fun setCustomerStatus(preferences: SharedPreferences, status: String) {
            preferences.edit().putString(CUSTOMER_STATUS, status).apply()
        }

        fun getCustomerStatus(preferences: SharedPreferences) = preferences.getString(CUSTOMER_STATUS, Constants.customerLoggedOut)
    }
}