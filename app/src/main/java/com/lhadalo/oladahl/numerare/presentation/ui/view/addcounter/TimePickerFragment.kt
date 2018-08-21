package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment() {
    private lateinit var onTimeSetListener: TimePickerDialog.OnTimeSetListener

    fun setListener(listener: TimePickerDialog.OnTimeSetListener) {
        onTimeSetListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()

        return TimePickerDialog(activity,
                onTimeSetListener,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        )
    }
}