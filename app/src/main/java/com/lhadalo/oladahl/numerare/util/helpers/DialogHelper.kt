package com.lhadalo.oladahl.numerare.util.helpers

import android.app.AlertDialog
import android.content.Context

object DialogHelper {

    fun confirmDelete(context: Context?, onConfirm: () -> Unit) {
        AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("OK") { _, _ -> onConfirm() }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
    }
}