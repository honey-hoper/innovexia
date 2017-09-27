package com.webhopers.innovexia.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import com.webhopers.innovexia.R

class CreateSlideDialog(context: Context) {
    init {
        AlertDialog.Builder(context)
                .setView(R.layout.dialog_create_slide)
                .create()
                .apply {
                    show()

                }
    }
}