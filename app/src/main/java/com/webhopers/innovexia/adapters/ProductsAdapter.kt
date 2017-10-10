package com.webhopers.innovexia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokenautocomplete.FilteredArrayAdapter
import com.webhopers.innovexia.models.Product
import kotlinx.android.synthetic.main.product_view.view.*

class ProductsAdapter(context: Context, val resource: Int, objects: MutableList<Product>) : FilteredArrayAdapter<Product>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resource, parent, false)
        view.product_name.text = getItem(position).name
        return view
    }


    override fun keepObject(obj: Product, characters: String): Boolean {
        val mask = characters.toLowerCase()
        return obj.name?.toLowerCase()?.contains(mask)!!

    }

}