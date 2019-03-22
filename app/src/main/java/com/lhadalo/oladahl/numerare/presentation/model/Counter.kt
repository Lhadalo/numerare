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

    override fun toString(): String {
        return "CounterItem(id=$id, title=$title, typeDesc=$typeDesc, counterValue=$counterValue)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CounterItem

        if (id != other.id) return false
        if (title != other.title) return false
        if (counterValue != other.counterValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + counterValue
        return result
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


