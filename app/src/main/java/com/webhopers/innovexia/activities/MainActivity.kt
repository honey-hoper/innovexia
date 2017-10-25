package com.webhopers.innovexia.activities

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.presentationActivity.PresentationActivity
import com.webhopers.innovexia.dialogs.ListSlidesDialog
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.SharedPreferenceService
import com.webhopers.innovexia.services.Syncher
import com.webhopers.innovexia.utils.Constants
import com.webhopers.innovexia.utils.show
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : Syncher.SyncherInterface, AppCompatActivity() {


    private val CATEGORIES = "CATEGORIES"


    private var backPressed = false
    private var timeElapsed = 0L
    private val INTERVAL = 2000

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
        am_profile_btn.setOnClickListener { startProfileActivity() }
        am_slide_btn.setOnClickListener { OpenListSlidesDialog() }
    }


    private fun setUpToolbar() {
        setSupportActionBar(am_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        var i = 0
        while (i < am_toolbar.childCount) {
            val v = am_toolbar.getChildAt(i)
            if (v is TextView) {
                val typeFace = Typeface.createFromAsset(assets, "fonts/poppins.ttf")
                v.setTypeface(typeFace)
                break
            }
            i++
        }
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

    override fun onBackPressed() {
        if (timeElapsed + INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        }

        makeToast("Press again to exit")
        timeElapsed = System.currentTimeMillis()
    }

    fun OpenListSlidesDialog() {
        val displayValues = RealmDatabaseService.getSlides().map { it.name }

        if (displayValues.isEmpty()) {
            Toast.makeText(this, "No Slide to select", Toast.LENGTH_SHORT).show()
            return
        }

        ListSlidesDialog(this, displayValues.toTypedArray(), true, listOf())
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
        startActivity(Intent(this, PresentationActivity::class.java))
    }

    fun startCreateVisitActivity() {
        startActivity(Intent(this, CreateVisitActivity::class.java))
    }

    fun startProfileActivity() {
        startActivity(Intent(this, ProfileActivity::class.java))
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
