package com.webhopers.innovexia.activities.mainActivity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.CreateVisitActivity
import com.webhopers.innovexia.activities.SplashActivity
import com.webhopers.innovexia.activities.presentationActivity.PresentationActivity
import com.webhopers.innovexia.models.ProductCategory
import com.webhopers.innovexia.services.Syncher
import com.webhopers.innovexia.utils.show
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : MainView, AppCompatActivity() {

    private val CATEGORIES = "CATEGORIES"


    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        initUI()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_log_out -> presenter.LogOut()
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpToolbar()
        am_product_btn.setOnClickListener { presenter.getCategories() }
        am_dcr_btn.setOnClickListener { startCreateVisitActivity() }
        am_sync_btn.setOnClickListener { Syncher.initiate() }
    }

    private fun setUpToolbar() {
        setSupportActionBar(am_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    /**
     *
     * view functions
     */
    override fun showProgressBar(bool: Boolean) {
        am_pbar.show(bool)
    }

    override fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getSharedPreferences(fileName: String) = getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override fun startPresentationActivity(list: List<ProductCategory>) {
        val filteredList = list.filter { it.publish }
        val intent = Intent(this, PresentationActivity::class.java)
        intent.putExtra(CATEGORIES, (filteredList as Serializable))
        startActivity(intent)
    }

    override fun startCreateVisitActivity() {
        startActivity(Intent(this, CreateVisitActivity::class.java))
    }

    override fun startSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

}
