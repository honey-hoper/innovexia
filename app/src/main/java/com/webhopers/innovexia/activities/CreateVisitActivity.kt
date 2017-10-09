package com.webhopers.innovexia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter

import com.webhopers.innovexia.R
import com.webhopers.innovexia.adapters.BuyerAutoCompleteAdapter
import com.webhopers.innovexia.models.Buyer
import com.webhopers.innovexia.utils.FakeData
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
}
