package com.lhadalo.oladahl.numerare.util.helpers

import android.app.AlertDialog
import android.content.Context
import com.lhadalo.oladahl.numerare.R

object DialogHelper {

    fun confirmDelete(context: Context?, onConfirm: () -> Unit) {
        context?.let {
            AlertDialog.Builder(it)
                    .setTitle(it.getText(R.string.dialog_delete_title))
                    .setMessage(it.getText(R.string.dialog_delete_message))
                    .setPositiveButton(it.getText(R.string.dialog_positive_button)) { _, _ -> onConfirm() }
                    .setNegativeButton(it.getText(R.string.dialog_negative_button)) { dialog, _ -> dialog.dismiss() }
                    .show()
        }

    }
}