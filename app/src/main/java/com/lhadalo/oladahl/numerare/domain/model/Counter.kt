package com.lhadalo.oladahl.numerare.domain.model

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import javax.inject.Inject

class CounterMapper @Inject constructor() {

    fun mapToPresentation(entity: CounterEntity) : CounterItem {
        val item = CounterItem(entity.id, entity.creationDate)
        item.title = entity.title
        item.typeDesc = entity.type
        item.counterValue = entity.value

        return item
    }

    fun mapToPresentation(counterList: List<CounterEntity>) = counterList.map { mapToPresentation(it) }

    fun mapToEntity(counterItem: CounterItem) : CounterEntity = CounterEntity(
            counterItem.title,
            counterItem.typeDesc,
            counterItem.counterValue,
            counterItem.creationDate,
            counterItem.id
    )
}