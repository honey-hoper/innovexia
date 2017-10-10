package com.webhopers.innovexia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.tokenautocomplete.FilteredArrayAdapter
import com.tokenautocomplete.TokenCompleteTextView

import com.webhopers.innovexia.R
import com.webhopers.innovexia.adapters.BuyerAutoCompleteAdapter
import com.webhopers.innovexia.adapters.ProductsAdapter
import com.webhopers.innovexia.models.Product
import com.webhopers.innovexia.utils.FakeData
import com.webhopers.innovexia.views.ProductCompletionView
import kotlinx.android.synthetic.main.activity_create_visit.*

class CreateVisitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_visit)

        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.default_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpToolbar()
        setUpBuyerField()
        setUpProductField()
    }

    private fun setUpToolbar() {
        setSupportActionBar(acv_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setUpBuyerField() {
        acv_buyer_field.setAdapter(BuyerAutoCompleteAdapter(FakeData.getBuyers()))
    }

    private fun setUpProductField() {
        acv_product_field.setAdapter(ProductsAdapter(this, R.layout.product_view, FakeData.getProducts().toMutableList()))
        acv_product_field.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select)
    }
}
