package com.lhadalo.oladahl.numerare.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    var TAG = "AlarmReceiverTAG"

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action != null && context != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                Log.d(TAG, "onReceive: BOOT_COMPLETED")
                //TODO Schedule alarm

                return
            }
        }

        Log.d(TAG, "Received Alarm!")
        /*
        if (context != null)
            NotificationHelper.showNotification(context, "", "", 1)
        */
    }
}