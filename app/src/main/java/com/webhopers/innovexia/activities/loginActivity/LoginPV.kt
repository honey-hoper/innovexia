package com.webhopers.innovexia.activities.loginActivity

import android.content.Context
import android.content.SharedPreferences
import android.support.design.widget.TextInputEditText
import android.view.View
import com.google.gson.Gson
import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.Customer
import com.webhopers.innovexia.models.CustomerCredentials
import com.webhopers.innovexia.models.CustomerLoginResponse
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.Constants
import com.webhopers.innovexia.utils.convertToCustomerRealm
import com.webhopers.innovexia.utils.value
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(val view: LoginView, val context: Context) {

    val emailField : TextInputEditText
    val passwordField: TextInputEditText

    init {
        emailField = view.getView(R.id.al_email_field) as TextInputEditText
        passwordField = view.getView(R.id.al_password_field) as TextInputEditText
    }

    /**
     *
     * validates text inputs
     * and calls login if validation
     * is successful
     */
    fun validateAndLogin() {
        val email = emailField.value()
        val password = passwordField.value()

        if (email.isEmpty()) {
            emailField.error = "Empty"
            return
        }

        if (password.isEmpty()) {
            passwordField.error = "Empty"
            return
        }

        view.showProgressbar(true)
        view.enableButtons(false)
        view.showErrorView(false)
        login(email, password)
    }

    /**
     *
     * log in
     */
    fun login(email: String, password: String) {
        val customerCredentials = CustomerCredentials(email, password)
        WooCommerceClient.getSimple().create(WooCommerce::class.java)
                .loginCustomer(customerCredentials)
                .clone()
                .enqueue(object : Callback<CustomerLoginResponse> {

                    override fun onResponse(call: Call<CustomerLoginResponse>, response: Response<CustomerLoginResponse>) {
                        if (response.isSuccessful) {
                            val id = response.body()?.data?.id!!
                            getCustomer(id)
                        } else {
                            view.showProgressbar(false)
                            view.enableButtons(true)
                            view.showErrorView(true, getErrorFromResponse(response.errorBody()?.string()!!))
                        }
                    }

                    override fun onFailure(call: Call<CustomerLoginResponse>, t: Throwable) {
                        view.showProgressbar(false)
                        view.enableButtons(true)
                        view.showErrorView(true, "${t.message}")
                    }
                })
    }

    /**
     *
     * if login method is successful
     * then it calls getCustomer
     * to get details of the customer
     */
    fun getCustomer(id: Long) {
        WooCommerceClient.get().create(WooCommerce::class.java)
                .getCustomer(id)
                .enqueue(object : Callback<Customer> {
                    override fun onFailure(call: Call<Customer>, t: Throwable) {

                        view.showProgressbar(false)
                        view.enableButtons(true)
                        view.showErrorView(true, "${t.message}")

                    }

                    override fun onResponse(call: Call<Customer>, response: Response<Customer>) {

                        view.showProgressbar(false)
                        view.enableButtons(true)

                        if (response.isSuccessful) {
                            setCustomerStatus(response.body()!!)
                            view.startSplashActivity()
                        } else {
                            view.showErrorView(true, "${response.code()}")
                        }
                    }
                })
    }

    /**
     *
     * converts error message to json object
     * to get login error
     */
    fun getErrorFromResponse(errorBody: String): String {
        val error = JSONObject(errorBody).get("code").toString()
        when(error) {
            "authentication_failed" -> return "Activation Pending"
            "invalid_email" -> return "Invalid Email"
            "invalid_username" -> return "Invalid Username"
            "incorrect_password" -> return "Incorrect Password"
        }
        return error
    }

    /**
     *
     * set Customer status to logged in
     * and save their details to database
     */
    fun setCustomerStatus(customer: Customer) {
        val preferences = view.getSharedPreferences(Constants.customerStatusFile)
        SharedPreferenceService.setCustomerStatus(preferences, Constants.customerLoggedIn)
        RealmDatabaseService.saveCustomer(customer.convertToCustomerRealm())
    }


}

interface LoginView {
    fun getView(id: Int): View
    fun getSharedPreferences(fileName: String): SharedPreferences
    fun showProgressbar(bool: Boolean)
    fun enableButtons(bool: Boolean)
    fun showErrorView(bool: Boolean, message: String = "Invalid Credentials")
    fun startSplashActivity()
}