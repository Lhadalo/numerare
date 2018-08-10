package com.lhadalo.oladahl.numerare.presentation.model

import androidx.databinding.ObservableField
import com.lhadalo.oladahl.numerare.data.CounterEntity

import javax.inject.Inject

class ObservableCounter {
    val title = ObservableField<String>()
    val type = ObservableField<String>()
    val value = ObservableField<Int>()
}

data class CounterItem(
        var title: String = "",
        var typeDesc: String = "",
        val counterValue: Int = 0
)

class CounterMapper @Inject constructor(private val observableCounter: ObservableCounter) {
    fun mapToPresentation(counter: CounterEntity): ObservableCounter {
        observableCounter.title.set(counter.title)
        observableCounter.type.set(counter.type)
        observableCounter.value.set(counter.value)

        return observableCounter
    }

    fun mapToEnitity(counterItem: CounterItem) : CounterEntity = CounterEntity(
            counterItem.title,
            counterItem.typeDesc,
            counterItem.counterValue
    )

}
