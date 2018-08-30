package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import com.lhadalo.oladahl.numerare.util.helpers.AFTERNOON
import com.lhadalo.oladahl.numerare.util.helpers.EVENING
import com.lhadalo.oladahl.numerare.util.helpers.MORNING
import com.lhadalo.oladahl.numerare.util.helpers.NOON
import kotlinx.android.synthetic.main.dialog_add_reminder.view.*
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


class AddReminderDialog(val fragment: Fragment, val callback: (ReminderItem) -> Unit) : TimePickerDialog.OnTimeSetListener {
    private val dialog: AlertDialog
    private lateinit var timeArray: Array<String>
    private lateinit var timeAdapter: ArrayAdapter<String>

    private lateinit var spinnerDate: Spinner
    private lateinit var spinnerTime: Spinner

    private var customHour = 0
    private var customMinute = 0

    companion object {
        const val TAG = "AddReminderDialogTAG"
        const val POSITION_TIME_OTHER = 4

        fun show(fragment: Fragment, callback: (ReminderItem) -> Unit) {
            AddReminderDialog(fragment, callback).show()
        }
    }

    init {
        dialog = AlertDialog.Builder(fragment.context)
                .setTitle(fragment.resources.getText(R.string.button_reminder_text_add))
                .setView(getLayout())
                .create()
    }

    private fun show() = dialog.show()

    private fun getLayout(): View {
        val view: View = LayoutInflater.from(fragment.context).inflate(R.layout.dialog_add_reminder, null)
        spinnerDate = view.spinner_date
        spinnerTime = view.spinner_time.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position == POSITION_TIME_OTHER) {
                        val timepicker = TimePickerFragment()
                        timepicker.setListener(this@AddReminderDialog)
                        timepicker.show(fragment.fragmentManager, "timepicker")
                    }
                }
            }
        }

        spinnerTime.adapter = createTimeAdapter()

        view.btn_positive.setOnClickListener { onClickPositive() }
        return view
    }

    private fun createTimeAdapter(): ArrayAdapter<String>? {
        fragment.context?.let { context ->
            timeArray = context.resources.getStringArray(R.array.spinner_times)
            timeAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, timeArray)

            return timeAdapter
        }
        return null
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        customHour = hour
        customMinute = minute

        val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        val time = OffsetTime.of(LocalTime.of(hour, minute), ZoneOffset.UTC)

        timeArray[POSITION_TIME_OTHER] = time.format(formatter)
        timeAdapter.notifyDataSetChanged()
    }

    private fun onClickPositive() {
        callback(ReminderItem(getRepeatingDate(), getReminderTime()))
        dialog.dismiss()
    }

    private fun getRepeatingDate(): Int {
        return spinnerDate.selectedItemPosition
    }

    private fun getReminderTime(): OffsetTime {
        return when (spinnerTime.selectedItemPosition) {
            MORNING -> OffsetTime.of(LocalTime.of(8, 0), ZoneOffset.UTC)
            NOON -> OffsetTime.of(LocalTime.of(12, 0), ZoneOffset.UTC)
            AFTERNOON -> OffsetTime.of(LocalTime.of(17, 0), ZoneOffset.UTC)
            EVENING -> OffsetTime.of(LocalTime.of(22, 0), ZoneOffset.UTC)
            else -> OffsetTime.of(LocalTime.of(customHour, customMinute), ZoneOffset.UTC)
        }
    }
}
