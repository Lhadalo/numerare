package com.lhadalo.oladahl.numerare.data.reset

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.format.DateTimeFormatter

object DateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val timeFormatter = DateTimeFormatter.ISO_OFFSET_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(it, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(dateTime: OffsetDateTime?): String? {
        return dateTime?.let {
            it.format(formatter)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetTime(dateTime: OffsetTime?): String? {
        return dateTime?.let {
            it.format(timeFormatter)
        }
    }

    @TypeConverter
    @JvmStatic
    fun toOffsetTime(value: String?): OffsetTime? {
        return value?.let {
            return timeFormatter.parse(it, OffsetTime::from)
        }
    }
}