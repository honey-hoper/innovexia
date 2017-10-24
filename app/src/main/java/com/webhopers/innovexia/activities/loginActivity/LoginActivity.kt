package com.webhopers.innovexia.activities.loginActivity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.ForgotPasswordActivity
import com.webhopers.innovexia.activities.RegisterActivity
import com.webhopers.innovexia.activities.SplashActivity
import com.webhopers.innovexia.utils.show
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : LoginView, AppCompatActivity() {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this, this)

        initUI()
    }

    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpToolbar()
        al_login_btn.setOnClickListener { presenter.validateAndLogin() }
        al_register_btn.setOnClickListener { startRegisterActivity() }
        al_forgot_password.setOnClickListener { startForgotPasswordActivity() }
    }

    private fun setUpToolbar() {
        setSupportActionBar(al_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var i = 0
        while (i < al_toolbar.childCount) {
            val v = al_toolbar.getChildAt(i)
            if (v is TextView) {
                val typeFace = Typeface.createFromAsset(assets, "fonts/poppins.ttf")
                v.setTypeface(typeFace)
                break
            }
            i++
        }
    }

    fun startRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun startForgotPasswordActivity() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    /**
     *
     * view functions
     */
    override fun getView(id: Int): View {
        when(id) {
            R.id.al_email_field -> return al_email_field
            R.id.al_password_field -> return al_password_field
        }
        return View(this)
    }

    override fun getSharedPreferences(fileName: String) = getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override fun showProgressbar(bool: Boolean) {
        al_pbar.show(bool)
    }

    override fun enableButtons(bool: Boolean) {
        al_login_btn.isEnabled = bool
        al_register_btn.isEnabled = bool
    }

    override fun showErrorView(bool: Boolean, message: String) {
        if (bool) al_error_view.text = message
        al_error_view.show(bool)
    }

    override fun startSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}
