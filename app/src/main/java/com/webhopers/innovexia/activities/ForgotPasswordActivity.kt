package com.webhopers.innovexia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.utils.show
import com.webhopers.innovexia.utils.value
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initUI()
    }

    /**
     *
     * validates input and calls sendPassword
     */
    fun validateAndSendPassword() {
        val email = afp_email_field.value()

        if (email.isEmpty()) {
            afp_email_field.error = "Empty"
            return
        }

        sendPassword(email)
    }

    /**
     *
     * sends password to passed email
     */
    fun sendPassword(email: String) {

    }

    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpToolbar()
        afp_send_password_btn.setOnClickListener { validateAndSendPassword() }
    }

    private fun setUpToolbar() {
        setSupportActionBar(afp_toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showProgressBar(bool: Boolean) {
        afp_pbar.show(bool)
    }

    fun showErrorView(bool: Boolean, message: String = "Unable to send") {
        afp_error_view.show(bool)
        afp_error_view.text = message
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun startLoginActivity() {
        finish()
    }
}
