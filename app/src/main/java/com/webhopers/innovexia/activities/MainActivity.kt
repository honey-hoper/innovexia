package com.webhopers.innovexia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter

import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.ProductCategory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val CATEGORIES = "CATEGORIES"

    private lateinit var map: Map<String, String>

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //extracting categories from intent
        @Suppress("UNCHECKED_CAST")
        val categories = intent.getSerializableExtra(CATEGORIES) as? List<ProductCategory>

        //converting category list to a map
        if (categories != null) {
            map = categories.associateBy(keySelector = {it.name!!}, valueTransform = {it.id!!})
        }

        //initializing ui
        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.default_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpAppBar()
        setUpNavDrawer()
    }

    private fun setUpAppBar() {
        setSupportActionBar(am_toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerToggle = ActionBarDrawerToggle(this, am_drawer,R.string.open, R.string.close)
        drawerToggle.syncState()
        am_drawer.addDrawerListener(drawerToggle)
    }

    private fun setUpNavDrawer() {
        am_nav_list.adapter = ArrayAdapter(this, R.layout.nav_list_item, R.id.nli_category_name, map.map { it.key })
    }
}