package com.webhopers.innovexia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.adapters.MyVisitsAdapter
import com.webhopers.innovexia.models.Visit
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.show
import kotlinx.android.synthetic.main.activity_my_visits.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyVisitsActivtiy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_visits)

        initUI()

        val customerId = RealmDatabaseService.getCustomer().id

        WooCommerceClient.getSimple().create(WooCommerce::class.java)
                .getCustomerVisits(customerId!!)
                .enqueue(object : Callback<List<Visit>> {
                    override fun onFailure(call: Call<List<Visit>>, t: Throwable) {
                        showProgressBar(false)
                        makeToast()
                    }

                    override fun onResponse(call: Call<List<Visit>>, response: Response<List<Visit>>) {
                        showProgressBar(false)
                        if (response.isSuccessful) {
                            setUpRecyclerView(response.body()!!)
                        } else {
                            makeToast("Failed due to ${response.code()}")
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
        showProgressBar(true)
    }

    private fun setUpToolbar() {
        setSupportActionBar(amv_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setUpRecyclerView(visits: List<Visit>) {
        val itemDecoration = DividerItemDecoration(this, LinearLayout.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider))

        amv_recycler_view.layoutManager = LinearLayoutManager(this)
        amv_recycler_view.addItemDecoration(itemDecoration)
        amv_recycler_view.adapter = MyVisitsAdapter(visits)
    }

    fun showProgressBar(bool: Boolean) {
        amv_pbar.show(bool)
    }

    fun makeToast(message: String = "Network Error") = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
