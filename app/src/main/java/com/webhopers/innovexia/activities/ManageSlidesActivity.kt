package com.webhopers.innovexia.activities

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.TextView

import com.webhopers.innovexia.R
import com.webhopers.innovexia.adapters.ManageSlidesAdapter
import com.webhopers.innovexia.services.RealmDatabaseService
import kotlinx.android.synthetic.main.activity_manage_slides.*

class ManageSlidesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_slides)

        initUI()
    }

    //ui functions
    private fun initUI() {
        setUpToolbar()
        setUpRecyclerView()
    }

    private fun setUpToolbar() {
        setSupportActionBar(ams_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var i = 0
        while (i < ams_toolbar.childCount) {
            val v = ams_toolbar.getChildAt(i)
            if (v is TextView) {
                val typeFace = Typeface.createFromAsset(assets, "fonts/poppins.ttf")
                v.setTypeface(typeFace)
                break
            }
            i++
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpRecyclerView() {
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider))

        ams_recycler_view.adapter = ManageSlidesAdapter(RealmDatabaseService.getSlides().toMutableList())
        ams_recycler_view.layoutManager = LinearLayoutManager(this)
        ams_recycler_view.addItemDecoration(itemDecoration)
        ams_recycler_view.setHasFixedSize(true)
    }
}
