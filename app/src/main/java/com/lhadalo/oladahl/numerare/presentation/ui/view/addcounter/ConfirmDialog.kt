package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.app.AlertDialog
import android.content.Context

class ConfirmDialog(val context: Context?) {

    fun confirmDelete(onConfirm: () -> Unit) {
        AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("OK") { _, _ -> onConfirm() }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
    }
}