package com.webhopers.innovexia.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokenautocomplete.TokenCompleteTextView
import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.Product

class ProductCompletionView(context: Context?, attrs: AttributeSet?) : TokenCompleteTextView<Product>(context, attrs) {

    override fun getViewForObject(product: Product?): View {
        val inflator = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.token, parent as ViewGroup, false) as TextView
        view.text = product?.name
        return view
    }

    override fun defaultObject(completionText: String): Product {
        return Product(name = completionText)

    }


}