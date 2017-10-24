package com.webhopers.innovexia.activities

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.Customer
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.show
import com.webhopers.innovexia.utils.value
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initUI()

    }

    /**
     *
     * validates the inputs, on success calls
     * register otherwise returns
     */
    fun validateAndRegister() {
        val username = ar_username_field.value()
        val email = ar_email_field.value()
        val password = ar_password_field.value()
        val confirmPassword = ar_confirm_password_field.value()

        if (username.isEmpty()) {
            ar_username_field.error = "Empty"
            return
        }

        if (email.isEmpty()) {
            ar_email_field.error = "Empty"
            return
        }

        if (password.isEmpty()) {
            ar_password_field.error = "Empty"
            return
        }

        if (confirmPassword.isEmpty()) {
            ar_confirm_password_field.error = "Empty"
            return
        }

        if (!isValidEmail(email)) {
            ar_email_field.error = "Invalid Email"
            return
        }

        if (password != confirmPassword) {
            ar_confirm_password_field.error = "Password does not match"
            return
        }

        showProgressBar(true)
        enableButtons(false)
        showErrorView(false)
        register(username, email, password)
    }

    /**
     *
     * checks for valid email
     */
    fun isValidEmail(email: String) = Pattern.compile("\\w+@\\w+\\.\\w+").matcher(email).find()

    /**
     *
     * register cutomer
     */
    fun register(username: String, email: String, password: String) {
        val customer = Customer(username = username, email = email, password = password)
        WooCommerceClient.get().create(WooCommerce::class.java)
                .createCustomer(customer)
                .enqueue(object : Callback<Customer> {
                    override fun onFailure(call: Call<Customer>, t: Throwable) {
                        showProgressBar(false)
                        enableButtons(true)
                        showErrorView(true, "${t.message}")
                    }

                    override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                        showProgressBar(false)
                        enableButtons(true)
                        if (response.isSuccessful) {
                            makeToast("Registered")
                            startLoginActivity()
                        } else {
                            showErrorView(true, "${response.code()}")
                        }
                    }

                })
    }


    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpToolbar()
        ar_register_btn.setOnClickListener { validateAndRegister() }
    }

    private fun setUpToolbar() {
        setSupportActionBar(ar_toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var i = 0
        while (i < ar_toolbar.childCount) {
            val v = ar_toolbar.getChildAt(i)
            if (v is TextView) {
                val typeFace = Typeface.createFromAsset(assets, "fonts/poppins.ttf")
                v.setTypeface(typeFace)
                break
            }
            i++
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showProgressBar(bool: Boolean) {
        ar_pbar.show(bool)
    }

    fun enableButtons(bool: Boolean) {
        ar_register_btn.isEnabled = bool
    }

    fun showErrorView(bool: Boolean, message: String = "Unable to Register") {
        ar_error_view.show(bool)
        ar_error_view.text = message
    }

    fun makeToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun startLoginActivity() {
        finish()
    }

}
