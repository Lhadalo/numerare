package com.lhadalo.oladahl.numerare.presentation.model

import org.threeten.bp.OffsetTime

/**
 * Class for information when reminders should be shown
 * @repeatingDate repeat every day, week, month, (year)
 * @time the time of day the reminder should be shown
 */
data class ReminderItem(
        val repeatingDate: Int,
        val time: OffsetTime
)