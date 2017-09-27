package com.webhopers.innovexia.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.stfalcon.frescoimageviewer.ImageViewer
import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.ImageUrl
import com.webhopers.innovexia.services.RealmDatabaseService
import kotlinx.android.synthetic.main.dialog_list_slides.*


class ListSlidesDialog(context: Context, displayValues: Array<String?>, openSlide: Boolean, urls: List<ImageUrl>) {
    init {
        AlertDialog.Builder(context)
                .setView(R.layout.dialog_list_slides)
                .create()
                .apply {
                    show()
                    dls_slide_picker.minValue = 0
                    dls_slide_picker.maxValue = displayValues.size - 1
                    dls_slide_picker.displayedValues = displayValues
                    dls_cancel_btn.setOnClickListener { dismiss() }
                    dls_select_btn.setOnClickListener {
                        val selectedValue = dls_slide_picker.value

                        if (openSlide) {
                            val slide = RealmDatabaseService.getSlide(displayValues[selectedValue]!!)
                            val urls = slide?.urls?.map { it.url }

                            ImageViewer.Builder(context, urls)
                                    .build()
                                    .show()
                            Fresco.initialize(context)
                        } else {
                            RealmDatabaseService.addToSlide(displayValues[selectedValue]!!, urls)
                            Toast.makeText(context, "Added to ${displayValues[selectedValue]}", Toast.LENGTH_SHORT).show()
                        }

                        dismiss()
                    }

                }
    }
}