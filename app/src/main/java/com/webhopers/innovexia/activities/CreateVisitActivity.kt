package com.webhopers.innovexia.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import com.tokenautocomplete.TokenCompleteTextView

import com.webhopers.innovexia.R
import com.webhopers.innovexia.adapters.BuyerAutoCompleteAdapter
import com.webhopers.innovexia.adapters.ProductsAdapter
import com.webhopers.innovexia.utils.FakeData
import kotlinx.android.synthetic.main.activity_create_visit.*
import java.text.SimpleDateFormat
import java.util.*

class CreateVisitActivity : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AppCompatActivity() {

    private var day = 0
    private var month = 0
    private var year = 0

    private var hour = 0
    private var min = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_visit)

        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.default_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun selectTime() {
        val dialog = TimePickerDialog(this, this, hour, min, false)
        dialog.show()
    }

    fun selectDate() {
        val dialog= DatePickerDialog(this, this,  year, month, day)
        dialog.show()
    }

    /**
     *
     * time and date change listener callbacks
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour =hourOfDay
        this.min = minute
        setTime()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day = dayOfMonth
        this.month = month + 1
        this.year = year
        setDate()
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

        acv_date_view.setOnClickListener { selectDate() }
        acv_time_view.setOnClickListener { selectTime() }
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
        acv_product_field.allowDuplicates(false)
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

}
