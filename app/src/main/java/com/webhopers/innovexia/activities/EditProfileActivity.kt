package com.webhopers.innovexia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.Customer
import com.webhopers.innovexia.models.MetaData
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.convertToCustomerRealm
import com.webhopers.innovexia.utils.show
import com.webhopers.innovexia.utils.value
import kotlinx.android.synthetic.main.activity_edit_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        type = intent.getStringExtra("TYPE")

        initUI()
    }

    private fun initUI() {
        setUpToolbar()
        setEditTextHintAndType()
        aep_update_btn.setOnClickListener { validate() }
    }

    private fun setUpToolbar() {
        setSupportActionBar(aep_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setEditTextHintAndType() {
        aep_input_field.hint = "Enter $type"
        if (type == "Phone") {
            aep_input_field.inputType = InputType.TYPE_CLASS_PHONE
            aep_input_field.filters = arrayOf(InputFilter.LengthFilter(10))
        } else if (type == "Email") aep_input_field.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
          else aep_input_field.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
    }

    fun validate() {
        val input = aep_input_field.value()

        if (input.isEmpty()) {
            aep_input_field.error = "Empty"
            return
        }

        showProgressBar(true)
        updateCustomer(input)

    }

    fun updateCustomer(input: String) {
        val customerId = RealmDatabaseService.getCustomer().id
        val customer = Customer(id = customerId)

        when (type) {
            "Email" -> customer.email = input
            "First Name" -> customer.firstName = input
            "Last Name" -> customer.lastName = input
            "Phone" -> customer.metaData = listOf(MetaData("phone1", input))
        }

        WooCommerceClient.get()
                .create(WooCommerce::class.java)
                .updateCustomer(customerId.toString(), customer)
                .enqueue(object : Callback<Customer> {
                    override fun onFailure(call: Call<Customer>, t: Throwable) {
                        showProgressBar(false)
                        makeToast("Network Error")
                    }

                    override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                        showProgressBar(false)
                        if (response.isSuccessful) {
                            RealmDatabaseService.saveCustomer(response.body()!!.convertToCustomerRealm())
                            makeToast("Updated!")
                        } else makeToast("Failed due to ${response.code()}")
                    }

                })
    }

    fun showProgressBar(bool: Boolean) {
        aep_pbar.show(bool)
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
