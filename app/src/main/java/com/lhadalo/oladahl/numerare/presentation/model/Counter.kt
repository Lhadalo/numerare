package com.lhadalo.oladahl.numerare.presentation.model

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import org.threeten.bp.OffsetDateTime

class CounterItem(var id: Long = 0, var creationDate: OffsetDateTime? = null) : BaseObservable(), Parcelable {

    @get:Bindable
    var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR._all)
        }

    @get:Bindable
    var typeDesc: String = "Count"
        set(value) {
            field = value
            notifyPropertyChanged(BR._all)
        }

    @get:Bindable
    var counterValue: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR._all)
        }

    var reminderItem: ReminderItem? = null



    constructor(pIn: Parcel) : this(pIn.readLong(), DateTimeConverter.toOffsetDateTime(pIn.readString())) {
        title = pIn.readString()
        typeDesc = pIn.readString()
        counterValue = pIn.readInt()
    }


    override fun describeContents() = 0


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(typeDesc)
        dest.writeInt(counterValue)
        dest.writeString(DateTimeConverter.fromOffsetDateTime(creationDate))
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


