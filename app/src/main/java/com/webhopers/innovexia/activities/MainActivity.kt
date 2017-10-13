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
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.services.Syncher
import com.webhopers.innovexia.utils.Constants
import com.webhopers.innovexia.utils.show
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : Syncher.SyncherInterface, AppCompatActivity() {


    private val CATEGORIES = "CATEGORIES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }

    private fun initUI() {
        setUpToolbar()
        am_product_btn.setOnClickListener { startPresentationActivity() }
        am_dcr_btn.setOnClickListener { startCreateVisitActivity() }
        am_sync_btn.setOnClickListener { Syncher.initiate(this) }
    }


    private fun setUpToolbar() {
        setSupportActionBar(am_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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


    fun LogOut() {
        val preferences = getSharedPreferences(Constants.customerStatusFile, Context.MODE_PRIVATE)
        SharedPreferenceService.setCustomerStatus(preferences, Constants.customerLoggedOut)
        RealmDatabaseService.removeCustomer()
        startSplashActivity()
    }

    fun showSmoothProgressBar(bool: Boolean) {
        am_spbar.show(bool)
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun startPresentationActivity() {
        val filteredList = RealmDatabaseService.getCategories().filter { it.publish!! }
        val intent = Intent(this, PresentationActivity::class.java)
        intent.putExtra(CATEGORIES, (filteredList as Serializable))
        startActivity(intent)
    }

    fun startCreateVisitActivity() {
        startActivity(Intent(this, CreateVisitActivity::class.java))
    }

    fun startSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    /**
     *
     * syncher interface methods
     */
    override fun preSync() {
        showSmoothProgressBar(true)
    }

    override fun postSync(message: String) {
        showSmoothProgressBar(false)
        makeToast(message)
    }
}
