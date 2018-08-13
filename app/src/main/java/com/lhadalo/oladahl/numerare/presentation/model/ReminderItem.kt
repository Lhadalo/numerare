package com.lhadalo.oladahl.numerare.presentation.model

import java.util.*

/**
 * Class for information when reminders should be shown
 * @repeatingDate Maps time interval with number to day, week, month, (year)
 * @time the time on a day the reminder should be shown
 */
class ReminderItem(
        val repeatingDate: Pair<Int, Int>,
        val time: GregorianCalendar
)