package com.webhopers.innovexia.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webhopers.innovexia.R
import com.webhopers.innovexia.services.GlideApp
import kotlinx.android.synthetic.main.square_image_view.view.*

class SelectableAdapter(val dataset: List<String?>) : RecyclerView.Adapter<SelectableAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GlideApp.with(holder.itemView.context)
                .load(dataset[position])
                .centerCrop()
                .into(holder.itemView.square_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.square_image_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataset.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}