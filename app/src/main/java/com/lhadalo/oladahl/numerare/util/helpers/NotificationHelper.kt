package com.lhadalo.oladahl.numerare.util.helpers

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.ui.activity.MainActivity
import java.util.*


object NotificationHelper {
    const val REMINDER_CHANNEL = "reminder_channel"
    const val REMINDER_REQUEST_CODE = 1
    const val COUNTER_ID = "counter_id"

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
                .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(REMINDER_REQUEST_CODE, notification)
    }

    fun <T : Any> createAlarm(context: Context, cls: Class<T>, hour: Int, minute: Int) {

        //Set time to schedule to notification
        val notifyTime = Calendar.getInstance()
        notifyTime.set(Calendar.HOUR_OF_DAY, hour)
        notifyTime.set(Calendar.MINUTE, minute)
        notifyTime.set(Calendar.SECOND, 0)

        //Cancel previously set alarms
        cancelAlarm(context, cls)

        //If notifyTime is before now, add one day
        if (notifyTime.before(Calendar.getInstance()))
            notifyTime.add(Calendar.DATE, 1)

        // Enable the receiver (disabled by default)
        context.packageManager.setComponentEnabledSetting(
                ComponentName(context, cls),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )

        //Intent to specify receiver (cls = AlarmReceiver)
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_REQUEST_CODE,
                Intent(context, cls),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Schedule the alarm at notifyTime
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                notifyTime.timeInMillis,
                pendingIntent
        )

        /*
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                notifyTime.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
        */
    }

    fun <T : Any> cancelAlarm(context: Context, cls: Class<T>) {

        // Disable the receiver
        context.packageManager.setComponentEnabledSetting(
                ComponentName(context, cls),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        )

        val pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_REQUEST_CODE,
                Intent(context, cls),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        pendingIntent.cancel()
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
