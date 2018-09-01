package com.lhadalo.oladahl.numerare.util

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lhadalo.oladahl.numerare.App
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.ACTION_MINUS
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.ACTION_PLUS
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.ACTION_REMINDER_NOTIFICATION
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.COUNTER_ID
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.COUNTER_REMINDER_TIME
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.COUNTER_TITLE
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.REMINDER_INTERVAL
import io.reactivex.Single
import org.threeten.bp.OffsetTime
import javax.inject.Inject

class AlarmReceiver @Inject constructor(val model: CounterModel) : BroadcastReceiver() {
    val TAG = "AlarmReceiverTAG"

    override fun onReceive(context: Context?, intent: Intent) {

        if (intent.action != null && context != null) {
            (context as App).injector.inject(this)
            when (intent.action) {
                ACTION_REMINDER_NOTIFICATION -> {

                    val title = intent.getStringExtra(COUNTER_TITLE)
                    val counterId = intent.getLongExtra(COUNTER_ID, -1)
                    val reminderTime = DateTimeConverter.toOffsetTime(intent.getStringExtra(COUNTER_REMINDER_TIME))
                    val interval = intent.getIntExtra(REMINDER_INTERVAL, -1)

                    NotificationHelper.showNotification(
                            context,
                            title,
                            String.format(
                                    context.resources.getString(R.string.reminder_notification_content),
                                    title
                            ),
                            0
                    )
                    scheduleNewAlarm(context, counterId, title, reminderTime, interval)
                }
                Intent.ACTION_BOOT_COMPLETED -> {

                }
                ACTION_MINUS -> {
                    Log.d(TAG, "Action Minus")
                }
                ACTION_PLUS -> {
                    Log.d(TAG, "Action Plus")

                }
            }
        }
    }

    private fun updateValue(counterId: Long, newValue: Int) {
        model.updateCount(counterId, newValue)
    }

    private fun scheduleNewAlarm(context: Context, counterId: Long, title: String, reminderTime: OffsetTime?, interval: Int) {
        NotificationHelper.createAlarm(context, counterId, title, interval, reminderTime)
    }
}