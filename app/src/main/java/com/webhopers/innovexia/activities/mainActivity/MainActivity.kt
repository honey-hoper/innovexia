package com.webhopers.innovexia.activities.mainActivity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.SplashActivity
import com.webhopers.innovexia.adapters.SelectableAdapter
import com.webhopers.innovexia.dialogs.ListSlidesDialog
import com.webhopers.innovexia.models.ProductCategory
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.utils.Constants
import com.webhopers.innovexia.utils.RecyclerViewDecorator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_list_item.view.*


class MainActivity : MainView, AppCompatActivity() {

    private val CATEGORIES = "CATEGORIES"

    private lateinit var map: Map<String, String>

    private lateinit var presenter: MainPresenter

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //presenter
        presenter = MainPresenter(this)

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
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) return true
        when(item.itemId) {
            R.id.action_open_slides -> OpenListSlidesDialog()
            R.id.action_log_out -> LogOut()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     *
     * opens list slides dialog
     */
    private fun OpenListSlidesDialog() {
        val displayValues = RealmDatabaseService.getSlides().map { it.name }
        if (displayValues.isEmpty()) {
            Toast.makeText(this, "No Slide to select", Toast.LENGTH_SHORT).show()
            return
        }
        ListSlidesDialog(this, displayValues.toTypedArray(), true, listOf())
    }

    /**
     *
     * log customer out
     */
    fun LogOut() {
        val preferences = getSharedPreferences(Constants.customerStatusFile, Context.MODE_PRIVATE)
        SharedPreferenceService.setCustomerStatus(preferences, Constants.customerLoggedOut)
        startSplashActivity()
    }


    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpAppBar()
        setUpNavDrawer()
        setUpRecyclerView()
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
        am_nav_list.setOnItemClickListener { adapterView, view, i, l ->
            val selectedCategory = map[view.nli_category_name.text.toString()]!!
            am_drawer.closeDrawers()
            am_recycler_view.adapter = null
            presenter.getProducts(selectedCategory)
        }
    }

    private fun setUpRecyclerView() {
        am_recycler_view.layoutManager = GridLayoutManager(this, 2)
        am_recycler_view.addItemDecoration(RecyclerViewDecorator(2, 5, true))
    }

    private fun startSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    /**
     *
     * view functions
     */
    override fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(bool: Boolean) {
        if (bool) am_pbar.visibility = View.VISIBLE
        else am_pbar.visibility = View.INVISIBLE
    }

    override fun setAdapter(list: List<String?>) {
        am_recycler_view.adapter = SelectableAdapter(list,this, this)
    }
}