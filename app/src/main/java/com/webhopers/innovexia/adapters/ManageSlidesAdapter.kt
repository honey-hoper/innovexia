package com.webhopers.innovexia.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.Slide
import com.webhopers.innovexia.services.RealmDatabaseService
import kotlinx.android.synthetic.main.manage_slide_list_item.view.*

class ManageSlidesAdapter(val dataset: MutableList<Slide>) : AdapterInterface, RecyclerView.Adapter<ManageSlidesAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position].name!!, dataset[position].urls!!.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.manage_slide_list_item, parent, false)
        return ViewHolder(v, this)
    }

    override fun getItemCount() = dataset.size

    override fun removeItem(position: Int) {
        RealmDatabaseService.removeSlide(dataset[position].name!!)
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View, val adapterInterface: AdapterInterface) : RecyclerView.ViewHolder(itemView) {

        fun bind(name: String, imageCount: Int) {
            itemView.msli_slide_name.text = name
            itemView.msli_img_count.text = "$imageCount"
            itemView.msli_delete_btn.setOnClickListener { adapterInterface.removeItem(adapterPosition) }
        }
    }


}

interface AdapterInterface {
    fun removeItem(position: Int)
}