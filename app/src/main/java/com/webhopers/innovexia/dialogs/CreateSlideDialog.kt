package com.webhopers.innovexia.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.webhopers.innovexia.R
import com.webhopers.innovexia.models.ImageUrl
import com.webhopers.innovexia.models.Slide
import com.webhopers.innovexia.services.RealmDatabaseService
import io.realm.RealmList
import kotlinx.android.synthetic.main.dialog_create_slide.*

class CreateSlideDialog(context: Context, urls: List<ImageUrl>) {
    init {
        AlertDialog.Builder(context)
                .setView(R.layout.dialog_create_slide)
                .create()
                .apply {
                    show()
                    dcs_cancel_btn.setOnClickListener { dismiss() }
                    dcs_create_btn.setOnClickListener {
                        val enteredName = dcs_slide_name_field.text.toString().trim()
                        if (enteredName.isEmpty()) {
                            dcs_slide_name_field.error = "Empty"
                            return@setOnClickListener
                        }
                        val realmList = RealmList<ImageUrl>()
                        urls.forEach { realmList.add(it) }
                        RealmDatabaseService.createSlide(Slide(enteredName, realmList))
                        Toast.makeText(context, "Created!", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }

                }
    }
}