package com.lhadalo.oladahl.numerare.util.helpers

import android.app.*
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
import androidx.core.content.res.ResourcesCompat
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import com.lhadalo.oladahl.numerare.presentation.ui.activity.MainActivity
import com.lhadalo.oladahl.numerare.util.AlarmReceiver
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZonedDateTime
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
    const val COUNTER_REMINDER_REPEATING_DAYS = "reminder_repeating"

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

    fun <T : Any> createAlarm(context: Context, cls: Class<T>,
                              notificationId: Long,
                              reminderItem: ReminderItem,
                              title: String?) {

        //Set time to schedule the notification
        val notifyTime = Calendar.getInstance()
        if (reminderItem.time != null) {
            notifyTime.set(Calendar.HOUR_OF_DAY, reminderItem.time.hour)
            notifyTime.set(Calendar.MINUTE, reminderItem.time.minute)
            notifyTime.set(Calendar.SECOND, 0)
        }

        //Cancel previously set alarms
        cancelAlarm(context, cls, notificationId.toInt())

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
                notificationId.toInt(),
                Intent(context, cls).apply {
                    putExtra(COUNTER_ID, notificationId)
                    title?.let { putExtra(COUNTER_TITLE, it) }
                    putExtra(COUNTER_REMINDER_TIME, DateTimeConverter.fromOffsetTime(reminderItem.time))
                    putExtra(COUNTER_REMINDER_REPEATING_DAYS, reminderItem.repeatingDate)
                    action = ACTION_REMINDER_NOTIFICATION
                },
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Schedule the alarm at notifyTime
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
                notifyTime.timeInMillis,
                pendingIntent
        )

        Log.d(TAG, "Set alarm for ${notifyTime[Calendar.DATE]}:${notifyTime[Calendar.HOUR_OF_DAY]}:${notifyTime[Calendar.MINUTE]}")
    }

    fun <T : Any> cancelAlarm(context: Context, cls: Class<T>, notificationId: Int) {

        // Disable the receiver
//        context.packageManager.setComponentEnabledSetting(
//                ComponentName(context, cls),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP
//        )

        //TODO Jag måste skicka med någon referens till respektive counter
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
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
