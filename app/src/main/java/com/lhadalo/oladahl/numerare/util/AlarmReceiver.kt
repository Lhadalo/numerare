package com.lhadalo.oladahl.numerare.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.ACTION_MINUS
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.ACTION_PLUS
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.ACTION_REMINDER_NOTIFICATION
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.COUNTER_ID
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.COUNTER_REMINDER_REPEATING_DAYS
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.COUNTER_REMINDER_TIME
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper.COUNTER_TITLE
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class AlarmReceiver : BroadcastReceiver() {
    val TAG = "AlarmReceiverTAG"

    override fun onReceive(context: Context?, intent: Intent) {
        val title = intent.getStringExtra(COUNTER_TITLE) ?: "error"
        val counterId = intent.getLongExtra(COUNTER_ID, 0)

        if (intent.action != null && context != null) {
            when(intent.action) {
                ACTION_REMINDER_NOTIFICATION -> {
                    NotificationHelper.showNotification(
                            context,
                            title,
                            String.format(
                                    context.resources.getString(R.string.reminder_notification_content),
                                    title
                            ),
                            counterId
                    )
                    scheduleNewAlarm(context, intent, counterId, title)
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

    private fun scheduleNewAlarm(context: Context, intent: Intent, counterId: Long, title: String) {
        Completable.fromAction {
            val reminderTime = DateTimeConverter.toOffsetTime(intent.getStringExtra(COUNTER_REMINDER_TIME))
            val reminderRepeating = intent.getIntExtra(COUNTER_REMINDER_REPEATING_DAYS, 0)
            NotificationHelper.createAlarm(
                    context,
                    AlarmReceiver::class.java,
                    counterId,
                    ReminderItem(reminderRepeating, reminderTime, true),
                    title
            )
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}