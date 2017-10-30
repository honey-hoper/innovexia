package com.webhopers.innovexia.activities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.*
import com.schibstedspain.leku.LocationPickerActivity
import com.tokenautocomplete.TokenCompleteTextView

import com.webhopers.innovexia.R
import com.webhopers.innovexia.adapters.BuyerAutoCompleteAdapter
import com.webhopers.innovexia.adapters.ProductsAdapter
import com.webhopers.innovexia.models.Buyer
import com.webhopers.innovexia.models.Product
import com.webhopers.innovexia.models.Visit
import com.webhopers.innovexia.services.RealmDatabaseService
import com.webhopers.innovexia.services.WooCommerce
import com.webhopers.innovexia.services.WooCommerceClient
import com.webhopers.innovexia.utils.show
import kotlinx.android.synthetic.main.activity_create_visit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CreateVisitActivity : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AppCompatActivity() {

    private var day = 0
    private var month = 0
    private var year = 0

    private var hour = 0
    private var min = 0

    private var address = "Please select location"

    private var buyerId: Int? = null
    private lateinit var buyerList: List<Buyer>
    private var products = mutableSetOf<Int>()

    private val MAP_API_KEY = "AIzaSyAiBzNcvnsTGZuyH7DBSmCcKHf-_1AD9qY"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_visit)

        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.default_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data?.getStringExtra(LocationPickerActivity.LOCATION_ADDRESS)
                if (address != null) {
                    this.address = address
                    setLocation()
                }
            }
        }
    }

    fun selectTime() {
        val dialog = TimePickerDialog(this, this, hour, min, false)
        dialog.show()
    }

    fun selectDate() {
        val dialog= DatePickerDialog(this, this,  year, month - 1, day)
        dialog.show()
    }

    fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            return
        }

        selectLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectLocation()
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    fun selectLocation() {
        val location = getLastKnownLocation()
        val lat = location?.latitude ?: 30.6532354
        val lng = location?.longitude ?: 76.8135977

        val intent = LocationPickerActivity.Builder()
                .withLocation(lat, lng)
                .withGeolocApiKey(MAP_API_KEY)
                .withSearchZone("en_in")
                .build(this)
        startActivityForResult(intent, 1)
    }

    fun getLastKnownLocation(): Location? {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        return location
    }

    /**
     *
     * time and date change listener callbacks
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.min = minute

        setTime()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day = dayOfMonth
        this.month = month + 1
        this.year = year
        setDate()
    }


    private fun submitVisit() {

        if (buyerId == null) {
            acv_buyer_field.error = "Select Valid Buyer"
            return
        }

        if (products.isEmpty()) {
            acv_product_field.error = "Select Valid Product"
            return
        }

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val calender = Calendar.getInstance()

        val currentDate = calender.time

        calender.set(year, month - 1, day, hour, min)
        val enteredDate = calender.time

        if (enteredDate.after(currentDate)) {
            Toast.makeText(this, "Enter correct Date/Time", Toast.LENGTH_SHORT).show()
            return
        }

        val date = dateFormatter.format(calender.time)

        if (address == "Please select location") {
            Toast.makeText(this, "Select Location", Toast.LENGTH_SHORT).show()
            return
        }


        val productIds = products.toList().toString().replace("[", "").replace("]", "")

        val customerId = RealmDatabaseService.getCustomer().id!!.toInt()

        val visit = Visit(customerId, buyerId, date.toString(), address, productIds)

        showProgressBar(true)
        disableSubmitButton(true)

        WooCommerceClient.getSimple().create(WooCommerce::class.java)
                .addVisit(visit)
                .enqueue(object : Callback<Any> {
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        showProgressBar(false)
                        disableSubmitButton(false)
                        Toast.makeText(this@CreateVisitActivity, "Failed ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        showProgressBar(false)
                        disableSubmitButton(false)
                        if (response.isSuccessful) {
                            Toast.makeText(this@CreateVisitActivity, "Successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CreateVisitActivity, "Failed ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }



    /**
     *
     * ui functions
     */
    private fun initUI() {
        setUpToolbar()
        setUpBuyerField()
        setUpProductField()
        setUpDateAndTime()
        setLocation()

        acv_edit_date.setOnClickListener { selectDate() }
        acv_edit_time.setOnClickListener { selectTime() }
        acv_edit_location.setOnClickListener { checkLocationPermission() }
        acv_submit_btn.setOnClickListener { submitVisit() }
    }

    private fun setUpToolbar() {
        setSupportActionBar(acv_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var i = 0
        while (i < acv_toolbar.childCount) {
            val v = acv_toolbar.getChildAt(i)
            if (v is TextView) {
                val typeFace = Typeface.createFromAsset(assets, "fonts/poppins.ttf")
                v.setTypeface(typeFace)
                break
            }
            i++
        }
    }

    private fun setUpBuyerField() {
        buyerList = RealmDatabaseService.getBuyers()
        acv_buyer_field.setAdapter(BuyerAutoCompleteAdapter(buyerList))

        acv_buyer_field.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position)
            buyerId = buyerList.find { it.name == item }?.id

        }
    }

    private fun setUpProductField() {
        acv_product_field.setAdapter(ProductsAdapter(this, R.layout.product_view, RealmDatabaseService.getProducts().filter { it.publish!! }.toMutableList()))
        acv_product_field.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select)
        acv_product_field.allowDuplicates(false)
        acv_product_field.setTokenListener(object : TokenCompleteTextView.TokenListener<Product> {
            override fun onTokenRemoved(token: Product) {
                if (token.id != null) {
                    products.remove(token.id!!.toInt())
                }
            }
            override fun onTokenAdded(token: Product) {
                if (token.id != null) {
                    products.add(token.id!!.toInt())
                }
            }
        })
    }

    private fun setUpDateAndTime() {
        //time
        val _hour = SimpleDateFormat("HH")
        val _min = SimpleDateFormat("mm")

        //date
        val _day = SimpleDateFormat("dd")
        val _month = SimpleDateFormat("MM")
        val _year = SimpleDateFormat("yyyy")

        val calender = Calendar.getInstance()

        hour = _hour.format(calender.time).toInt()
        min = _min.format(calender.time).toInt()

        day = _day.format(calender.time).toInt()
        month = _month.format(calender.time).toInt()
        year = _year.format(calender.time).toInt()

        setTime()
        setDate()
    }

    private fun setTime() {
        acv_time_view.text = "$hour:$min"
    }

    private fun setDate() {
        acv_date_view.text = "$day/$month/$year"
    }

    private fun setLocation() {
        acv_location_view.text = address
    }

    fun disableSubmitButton(bool: Boolean) {
        acv_submit_btn.isEnabled= bool
    }

    fun showProgressBar(bool: Boolean) {
        acv_pbar.show(bool)
    }

}
