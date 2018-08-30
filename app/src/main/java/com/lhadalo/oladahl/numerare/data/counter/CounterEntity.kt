package com.lhadalo.oladahl.numerare.data.counter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import org.jetbrains.annotations.NotNull
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime


@Entity(tableName = "counter_table")
data class CounterEntity(
        @ColumnInfo(name = "title") val title: String?,
        @ColumnInfo(name = "type")  val type: String?,
        @ColumnInfo(name = "value") val value: Int,
        @ColumnInfo(name = "creation_date") val creationDate: OffsetDateTime?,
        @ColumnInfo(name = "reminder_repeating_date") val reminderRepeatingDate: Int?,
        @ColumnInfo(name = "reminder_time") val reminderTime: OffsetTime?,
        @ColumnInfo(name = "reminder_is_set") val reminderIsSet: Boolean?,
        @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") val id: Long = 0
)