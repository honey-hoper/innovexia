package com.webhopers.innovexia.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webhopers.innovexia.R
import com.webhopers.innovexia.activities.VisitActivity
import com.webhopers.innovexia.models.Visit
import com.webhopers.innovexia.services.RealmDatabaseService
import kotlinx.android.synthetic.main.visit_item_view.view.*

class MyVisitsAdapter(val dataset: List<Visit>) : RecyclerView.Adapter<MyVisitsAdapter.ViewHolder>() {

    val buyers: Map<Int?, String?>

    init {
        buyers = RealmDatabaseService.getBuyers().associateBy(keySelector = {it.id}, valueTransform = {it.name})
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.visit_item_view, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position], buyers[dataset[position].buyerId]!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(visit: Visit, buyerName: String) {
            itemView.viv_date_view.text = visit.dateTime
            itemView.viv_buyer_view.text = buyerName
            itemView.viv_address_view.text = visit.location

            itemView.setOnClickListener {
                val intent = Intent(it.context, VisitActivity::class.java)
                intent.putExtra("productIds", visit.productIds)
                ContextCompat.startActivity(it.context, intent, null)
            }
        }
    }
}