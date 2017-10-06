package com.webhopers.innovexia.utils

import android.support.design.widget.TextInputEditText
import android.view.View

fun View.show(bool: Boolean) {
    if (bool) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun TextInputEditText.value() = this.text.toString().trim()



