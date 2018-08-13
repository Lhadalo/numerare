package com.lhadalo.oladahl.numerare.presentation.model

import com.lhadalo.oladahl.numerare.data.CounterEntity

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import javax.inject.Inject

class CounterItem(var id: Int = 0) : BaseObservable(), Parcelable {


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


    constructor(pIn: Parcel) : this(pIn.readInt()) {
        title = pIn.readString()
        typeDesc = pIn.readString()
        counterValue = pIn.readInt()
    }


    override fun describeContents() = 0


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
            return arrayOfNulls(size)
        }
    }
}

class CounterMapper @Inject constructor() {

    fun mapToPresentation(entity: CounterEntity) : CounterItem {
        val item = CounterItem(entity.id)
        item.title = entity.title
        item.typeDesc = entity.type
        item.counterValue = entity.value

        return item
    }

    fun mapToPresentation(counterList: List<CounterEntity>) = counterList.map { mapToPresentation(it) }

    fun mapToEnitity(counterItem: CounterItem) : CounterEntity = CounterEntity(
            counterItem.title,
            counterItem.typeDesc,
            counterItem.counterValue,
            counterItem.id
    )
}
