package com.webhopers.innovexia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.Buyer
import kotlinx.android.synthetic.main.buyer_view.view.*

class BuyerAutoCompleteAdapter(val originalList: List<Buyer>) : Filterable, BaseAdapter() {

    var tempList = originalList


    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = mutableListOf<Buyer>()
                if (constraint == null || constraint.length == 0) {
                    results.values = originalList
                    results.count = originalList.size
                } else {
                    originalList.forEach {
                        val data = it.name!!
                        if (data.toLowerCase().contains(constraint.toString())) {
                            filteredList.add(it)
                        }
                    }
                    results.values = filteredList
                    results.count = filteredList.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tempList = results?.values as List<Buyer>
                notifyDataSetChanged()
            }

        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            v = LayoutInflater.from(parent.context).inflate(R.layout.buyer_view, parent, false)

            v.setOnTouchListener {view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
                }
                return@setOnTouchListener false
            }

            v.bv_buyer_name.text = tempList[position].name
            v.bv_buyer_type.text = "(${if (tempList[position].type == 1) "Doctor" else "Chemist"})"
            v.bv_buyer_address.text = tempList[position].address
            return v
        }
        v.bv_buyer_name.text = tempList[position].name
        v.bv_buyer_type.text = "(${if (tempList[position].type == 1) "Doctor" else "Chemist"})"
        v.bv_buyer_address.text = tempList[position].address
        return v
    }

    override fun getItem(position: Int): Any {
        return tempList[position].name!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return tempList.size
    }
}