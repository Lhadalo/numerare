package com.lhadalo.oladahl.numerare.presentation.model

import androidx.databinding.ObservableField
import com.lhadalo.oladahl.numerare.data.Counter

import javax.inject.Inject

class ObservableCounter {
    val title = ObservableField<String>()
    val type = ObservableField<String>()
    val value = ObservableField<Int>()
}


class CounterMapper @Inject constructor(private val observableCounter: ObservableCounter) {
    fun mapToPresentation(counter: Counter): ObservableCounter {
        observableCounter.title.set(counter.title)
        observableCounter.type.set(counter.type)
        observableCounter.value.set(counter.value)

        return observableCounter
    }

}
