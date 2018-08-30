package com.lhadalo.oladahl.numerare.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import org.threeten.bp.OffsetDateTime

data class CounterItem(
        var id: Long = 0,
        var creationDate: OffsetDateTime? = null,
        var title: String? = null,
        var typeDesc: String? = null,
        var counterValue: Int = 0,
        var reminderItem: ReminderItem? = null
        ) : Parcelable {

    constructor(pIn: Parcel) : this(
            pIn.readLong(),
            DateTimeConverter.toOffsetDateTime(pIn.readString()),
            pIn.readString(),
            pIn.readString(),
            pIn.readInt(),
            pIn.readParcelable(ReminderItem::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(DateTimeConverter.fromOffsetDateTime(creationDate))
        dest.writeString(title)
        dest.writeString(typeDesc)
        dest.writeInt(counterValue)
        dest.writeParcelable(reminderItem, flags)

    }

    companion object CREATOR : Parcelable.Creator<CounterItem> {
        override fun createFromParcel(pIn: Parcel): CounterItem {
            return CounterItem(pIn)
        }

        override fun newArray(size: Int): Array<CounterItem?> {
            return arrayOfNulls(size)
        }
    }
}


