package com.lhadalo.oladahl.numerare.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import org.threeten.bp.OffsetTime

/**
 * Class for information when reminders should be shown
 * @interval repeat every day, week, month, (year)
 * @time the time of day the reminder should be shown
 */
data class ReminderItem(
        val interval: Int = 0,
        val time: OffsetTime?,
        val setReminder: Boolean = true
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            DateTimeConverter.toOffsetTime(parcel.readString()),
            parcel.readInt() == 1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(interval)
        parcel.writeString(DateTimeConverter.fromOffsetTime(time))
        parcel.writeInt(if (setReminder) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReminderItem> {
        override fun createFromParcel(parcel: Parcel): ReminderItem {
            return ReminderItem(parcel)
        }

        override fun newArray(size: Int): Array<ReminderItem?> {
            return arrayOfNulls(size)
        }
    }
}