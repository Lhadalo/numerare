package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddReminderDialog.Companion.POSITION_DATE_OTHER
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddReminderDialog.Companion.POSITION_TIME_OTHER
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddReminderDialog.Companion.SPINNER_DATE
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddReminderDialog.Companion.SPINNER_TIME
import com.lhadalo.oladahl.numerare.util.helpers.*
import kotlinx.android.synthetic.main.dialog_add_reminder.view.*
import java.util.*

class AddReminderDialog(val context: Context?, val callback: (ReminderItem) -> Unit) {
    private lateinit var dialog: AlertDialog
    private lateinit var spinnerDate: Spinner
    private lateinit var spinnerTime: Spinner

    companion object {
        const val SPINNER_DATE = 1
        const val POSITION_DATE_OTHER = 3

        const val SPINNER_TIME = 2
        const val POSITION_TIME_OTHER = 4
    }

    fun show() {
        dialog = AlertDialog.Builder(context).setView(getLayout()).create()
        dialog.show()
    }

    private fun getLayout(): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_add_reminder, null)
        spinnerDate = view.spinner_date.apply { onItemSelectedListener = ItemSelected(SPINNER_DATE) }
        spinnerTime = view.spinner_time.apply { onItemSelectedListener = ItemSelected(SPINNER_TIME) }

        view.btn_positive.setOnClickListener(::onClickPositive)
        return view
    }

    private fun onClickPositive(view: View?) {
        callback(ReminderItem(getRepeatingDate(), getReminderTime()))
        dialog.dismiss()
    }

    private fun getRepeatingDate() : Pair<Int, Int> {
        return when(spinnerDate.selectedItemPosition) {
            DAY -> 1 to DAY
            WEEK -> 1 to WEEK
            MONTH -> 1 to MONTH
            else -> TODO("Get values from layout")
        }
    }

    private fun getReminderTime() : GregorianCalendar {

        return when(spinnerTime.selectedItemPosition) {
            MORNING -> GregorianCalendar(0,0,0, 8,0)
            NOON -> GregorianCalendar(0,0,0, 12,0)
            AFTERNOON -> GregorianCalendar(0,0,0, 17,0)
            EVENING -> GregorianCalendar(0,0,0, 22,0)
            else -> TODO("Get value from saved parameter")
        }
    }
}

class ItemSelected(private val spinnerType: Int) : AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (spinnerType) {
            SPINNER_DATE -> if (position == POSITION_DATE_OTHER) TODO("Switch layout to number -> day, week, month")
            SPINNER_TIME -> if (position == POSITION_TIME_OTHER) TODO("Show time picker dialog")
        }
    }
}