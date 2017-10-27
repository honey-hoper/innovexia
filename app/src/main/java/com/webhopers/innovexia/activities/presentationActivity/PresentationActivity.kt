package com.webhopers.innovexia.activities.presentationActivity

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.ManageSlidesActivity
import com.webhopers.innovexia.adapters.SelectableAdapter
import com.webhopers.innovexia.dialogs.ListSlidesDialog
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.utils.RecyclerViewDecorator
import kotlinx.android.synthetic.main.activity_presentation.*
import kotlinx.android.synthetic.main.nav_list_item.view.*



class PresentationActivity : PresentationView, AppCompatActivity() {

    private lateinit var map: Map<String, String>

    private lateinit var presenter: PresentationPresenter

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)

        //presenter
        presenter = PresentationPresenter(this)

        //product categories
        map = RealmDatabaseService.getCategories()
                .filter { it.publish!! }
                .associateBy( keySelector = {specialCharacterParser(it.name!!)}, valueTransform = {it.id!!} )

        //initializing ui
        initUI()
    }

    /**
     *
     * replaces html special characters from string
     * used to replace special characters from categories
     * and then categories are put in map
     */
    fun specialCharacterParser(str: String): String {
        var newStr = str
        val specialCharacter = listOf(
                "&amp;" to "&",
                "&lt;" to "<",
                "&gt;" to ">",
                "&quot;" to "\"",
                "&apos;" to "'"
        )

        specialCharacter.forEach {
            if (str.contains(it.first))
                newStr = str.replace(it.first, it.second)

        }

        return newStr
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.presentation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) return true
        when(item.itemId) {
            R.id.action_open_slides -> OpenListSlidesDialog()
            R.id.action_manage_slides -> startManageSlidesActivity()
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

    private fun startManageSlidesActivity() {
        startActivity(Intent(this, ManageSlidesActivity::class.java))
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
        setSupportActionBar(ap_toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var i = 0
        while (i < ap_toolbar.childCount) {
            val v = ap_toolbar.getChildAt(i)
            if (v is TextView) {
                val typeFace = Typeface.createFromAsset(assets, "fonts/poppins.ttf")
                v.setTypeface(typeFace)
                break
            }
            i++
        }

        drawerToggle = ActionBarDrawerToggle(this, ap_drawer,R.string.open, R.string.close)
        drawerToggle.syncState()
        ap_drawer.addDrawerListener(drawerToggle)
    }

    private fun setUpNavDrawer() {
        ap_nav_list.adapter = ArrayAdapter(this, R.layout.nav_list_item, R.id.nli_category_name, map.map { it.key  })
        ap_nav_list.setOnItemClickListener { adapterView, view, i, l ->
            val selectedCategory = map[view.nli_category_name.text.toString()]!!
            ap_recycler_view.adapter = null
            presenter.getProducts(selectedCategory)
            ap_drawer.closeDrawers()
        }
        ap_drawer.openDrawer(Gravity.START)
    }

    private fun setUpRecyclerView() {
        ap_recycler_view.layoutManager = GridLayoutManager(this, 2)
        ap_recycler_view.addItemDecoration(RecyclerViewDecorator(2, 5, true))
    }

    /**
     *
     * view functions
     */
    override fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(bool: Boolean) {
        if (bool) ap_pbar.visibility = View.VISIBLE
        else ap_pbar.visibility = View.INVISIBLE
    }

    override fun setAdapter(list: List<String?>) {
        ap_recycler_view.adapter = SelectableAdapter(list,this, this)
    }
}