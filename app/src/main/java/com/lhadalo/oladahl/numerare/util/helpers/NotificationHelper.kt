package com.lhadalo.oladahl.numerare.util.helpers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.ui.activity.MainActivity
import com.lhadalo.oladahl.numerare.util.AlarmReceiver
import org.threeten.bp.OffsetTime
import java.util.*


object NotificationHelper {
    const val TAG = "NotificationHelper"
    const val REMINDER_CHANNEL = "reminder_channel"
    const val REMINDER_REQUEST_CODE = 1
    const val ACTION_REMINDER_NOTIFICATION = "action_reminder_notification"
    const val ACTION_PLUS = "action_plus"
    const val ACTION_MINUS = "action_minus"
    const val COUNTER_ID = "counter_id"
    const val COUNTER_TITLE = "counter_title"
    const val COUNTER_REMINDER_TIME = "reminder_time"
    const val REMINDER_INTERVAL = "reminder_interval"

    fun showNotification(context: Context, title: String, content: String, counterId: Long) {

        //Intent what to do when clicking notification. Pass counterId to launch
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(COUNTER_ID, counterId)

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(
                REMINDER_REQUEST_CODE,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL)
                .setSmallIcon(R.drawable.ic_adb_black_24dp)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .addAction(R.drawable.ic_adb_black_24dp, "-1",
                        PendingIntent.getBroadcast(
                                context,
                                0,
                                Intent(context, AlarmReceiver::class.java).apply {
                                    action = ACTION_MINUS
                                    putExtra(COUNTER_ID, counterId)
                                }
                                , 0
                        ))
                .addAction(R.drawable.ic_adb_black_24dp, "+1",
                        PendingIntent.getBroadcast(
                                context,
                                0,
                                Intent(context, AlarmReceiver::class.java).apply {
                                    action = ACTION_PLUS
                                    putExtra(COUNTER_ID, counterId)
                                }
                                , 0
                        ))
                .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(REMINDER_REQUEST_CODE, notification)
    }


    fun createAlarm(context: Context, counterId: Long, title: String?, interval: Int, reminderTime: OffsetTime?) {

        //Set time to schedule the notification
        val notifyTime = Calendar.getInstance()
        if (reminderTime != null) {
            notifyTime.set(Calendar.HOUR_OF_DAY, reminderTime.hour)
            notifyTime.set(Calendar.MINUTE, reminderTime.minute)
            notifyTime.set(Calendar.SECOND, 0)
        }

        //Cancel previously set alarms
        cancelAlarm(context, counterId, title, interval, reminderTime)

        //If notifyTime is before now, add one day
        if (notifyTime.before(Calendar.getInstance()))
            notifyTime.add(Calendar.DATE, 1)

        // Enable the receiver (disabled by default)
        context.packageManager.setComponentEnabledSetting(
                ComponentName(context, AlarmReceiver::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )

        //Create PendingIntent with counterId as notification id.
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                counterId.toInt(),
                createIntent(context, counterId, title, interval, reminderTime),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Schedule the alarm at notifyTime
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                notifyTime.timeInMillis,
                pendingIntent
        )

        Log.d(TAG, "Created Alarm: ${notifyTime.toString()}")
    }

    fun cancelAlarm(context: Context, counterId: Long, title: String?, interval: Int, reminderTime: OffsetTime?) {
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                counterId.toInt(),
                createIntent(context, counterId, title, interval, reminderTime),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        pendingIntent.cancel()
        Log.d(TAG, "Cancel Reminder")
    }

    private fun createIntent(context: Context, counterId: Long, title: String?, interval: Int, reminderTime: OffsetTime?): Intent {
        return Intent(context, AlarmReceiver::class.java).apply {
            putExtra(COUNTER_ID, counterId)
            putExtra(COUNTER_TITLE, title)
            putExtra(COUNTER_REMINDER_TIME, DateTimeConverter.fromOffsetTime(reminderTime))
            putExtra(REMINDER_INTERVAL, interval)
            action = ACTION_REMINDER_NOTIFICATION
        }
    }

    fun setupNotificationChannels(context: Context) {
        createReminderNotificationChannel(context)
    }

    private fun createReminderNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    REMINDER_CHANNEL,
                    context.getString(R.string.reminder_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = context.getString(R.string.reminder_channel_description)

            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
}
