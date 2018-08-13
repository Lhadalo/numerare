package com.lhadalo.oladahl.numerare.presentation.model

import androidx.databinding.ObservableField
import com.lhadalo.oladahl.numerare.data.CounterEntity

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.util.*
import javax.inject.Inject




class CounterItem(var id: Int = 0) : BaseObservable(), Parcelable {


    @get:Bindable
    var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.viewmodel)
        }


    var typeDesc: String = "Count"
        set(value) {
            field = value
            notifyPropertyChanged(BR.viewmodel)
        }


    var counterValue: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.viewmodel)
        }


    constructor(pIn: Parcel) : this(pIn.readInt()) {
        title = pIn.readString()
        typeDesc = pIn.readString()
        counterValue = pIn.readInt()
    }


    override fun describeContents(): Int {

        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(typeDesc)
        dest.writeInt(counterValue)
    }

    companion object CREATOR : Parcelable.Creator<CounterItem> {
        override fun createFromParcel(pIn: Parcel): CounterItem {
            return CounterItem(pIn)
        }

        override fun newArray(size: Int): Array<CounterItem?> {
            return arrayOfNulls<CounterItem>(size)

        }
    }
}

class CounterMapper @Inject constructor() {

    fun mapToItem(entity: CounterEntity) : CounterItem {
        val item = CounterItem(entity.id)
        item.title = entity.title
        item.typeDesc = entity.type
        item.counterValue = entity.value

        return item
    }

    fun mapToItem(counterList: List<CounterEntity>) = counterList.map { mapToItem(it) }

    fun mapToEnitity(counterItem: CounterItem) : CounterEntity = CounterEntity(
            counterItem.title,
            counterItem.typeDesc,
            counterItem.counterValue
    )
}
