package com.webhopers.innovexia.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.ProductCategory
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class SplashActivity : AppCompatActivity() {

    private val CATEGORIES = "CATEGORIES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        getCategories()
    }

    /**
     *
     * this method is called in oncreate
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
                            this@SplashActivity.filter(response.body()!!)
                        } else {
                            makeToast("Could not fetch categories: ${response.code()}")
                        }
                    }
                })
    }

    /**
     *
     * this method is called in getCategories
     * it filters out categories which should
     * not be shown
     */
    fun filter(list: List<ProductCategory>) {
        startMainActivity(list)
    }

    /**
     *
     * ui functions
     */
    fun showProgressBar(bool: Boolean) {
        if (bool) as_pbar.visibility = View.VISIBLE
        else as_pbar.visibility = View.INVISIBLE
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun startMainActivity(list: List<ProductCategory>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(CATEGORIES, (list as Serializable))
        startActivity(intent)
        finish()
    }

}
