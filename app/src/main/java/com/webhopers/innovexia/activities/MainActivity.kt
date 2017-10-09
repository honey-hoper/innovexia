package com.webhopers.innovexia.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.presentationActivity.PresentationActivity
import com.webhopers.innovexia.models.ProductCategory
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.Constants
import com.webhopers.innovexia.utils.show
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private val CATEGORIES = "CATEGORIES"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_log_out -> LogOut()
        }
        return super.onOptionsItemSelected(item)
    }









    /**
     *
     * it fetches the product categories from woocommerce
     * and passes it to filter function
     */
    private fun getCategories() {
        showProgressBar(true)
        WooCommerceClient.get()
                .create(WooCommerce::class.java)
                .categories()
                .enqueue(object : Callback<List<ProductCategory>> {
                    override fun onFailure(call: Call<List<ProductCategory>>, t: Throwable) {
                        showProgressBar(false)
                        makeToast("Could not fetch categories: ${t.message}")
                    }

                    override fun onResponse(call: Call<List<ProductCategory>>, response: Response<List<ProductCategory>>) {
                        showProgressBar(false)
                        if (response.isSuccessful) {
                            this@MainActivity.startPresentationActivity(response.body()!!)
                        } else {
                            makeToast("Could not fetch categories: ${response.code()}")
                        }
                    }
                })
    }

    /**
     *
     * log customer out
     */
    fun LogOut() {
        val preferences = getSharedPreferences(Constants.customerStatusFile, Context.MODE_PRIVATE)
        SharedPreferenceService.setCustomerStatus(preferences, Constants.customerLoggedOut)
        RealmDatabaseService.removeCustomer()
        startSplashActivity()
    }







    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpToolbar()
        btn.setOnClickListener { getCategories() }
    }

    private fun setUpToolbar() {
        setSupportActionBar(am_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun startSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    fun startPresentationActivity(list: List<ProductCategory>) {
        val filteredList = list.filter { it.publish }
        val intent = Intent(this, PresentationActivity::class.java)
        intent.putExtra(CATEGORIES, (filteredList as Serializable))
        startActivity(intent)
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar(bool: Boolean) {
        am_pbar.show(bool)
    }
}
