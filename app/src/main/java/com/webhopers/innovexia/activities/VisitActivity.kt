package com.webhopers.innovexia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.webhopers.innovexia.R
import com.webhopers.innovexia.services.RealmDatabaseService
import kotlinx.android.synthetic.main.activity_visit.*

class VisitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        initUI()

        val products = intent.getStringExtra("productIds").split(",")
        products.forEach {
            val product = RealmDatabaseService.getProduct(it.trim())
            av_text_view.append(product?.name + "\n")
        }
    }

    /**
     *
     * ui functions
     */

    private fun initUI() {
        setUpToolbar()
    }

    private fun setUpToolbar() {
        setSupportActionBar(av_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
