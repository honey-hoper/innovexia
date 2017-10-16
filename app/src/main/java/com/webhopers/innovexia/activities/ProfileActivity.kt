package com.webhopers.innovexia.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.CustomerRealm
import com.webhopers.innovexia.services.RealmDatabaseService
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val TYPE = "TYPE"

    private val EMAIL = "Email"
    private val FIRST_NAME = "First Name"
    private val LAST_NAME = "Last Name"
    private val PHONE = "Phone"

    lateinit var customer: CustomerRealm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        customer = RealmDatabaseService.getCustomer()

        initUI()
    }

    override fun onStart() {
        super.onStart()
        setUpFields()
    }

    private fun initUI() {
        setUpToolbar()

        apro_edit_email.setOnClickListener { startEditProfileActivity(EMAIL) }
        apro_edit_first_name.setOnClickListener { startEditProfileActivity(FIRST_NAME) }
        apro_edit_last_name.setOnClickListener { startEditProfileActivity(LAST_NAME) }
        apro_edit_phone.setOnClickListener { startEditProfileActivity(PHONE) }

    }

    private fun setUpToolbar() {
        setSupportActionBar(apro_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpFields() {
        apro_username.text = customer.username ?: ""
        apro_email.text = customer.email ?: ""
        apro_first_name.text = customer.firstName ?: ""
        apro_last_name.text = customer.lastName ?: ""
    }

    fun startEditProfileActivity(intentData: String) {
        val intent = Intent(this, EditProfileActivity::class.java)
        intent.putExtra(TYPE, intentData)
        startActivity(intent)
    }
}
