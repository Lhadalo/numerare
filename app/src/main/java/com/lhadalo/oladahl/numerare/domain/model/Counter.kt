package com.lhadalo.oladahl.numerare.domain.model

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import javax.inject.Inject

class CounterMapper @Inject constructor() {

    fun mapToPresentation(entity: CounterEntity): CounterItem {
        return CounterItem(
                entity.id,
                entity.creationDate,
                entity.title,
                entity.type,
                entity.value,
                if (entity.reminderIsSet) ReminderItem(
                        entity.reminderRepeatingDate,
                        entity.reminderTime,
                        entity.reminderIsSet)
                else null
        )
    }

    fun mapToPresentation(counterList: List<CounterEntity>) = counterList.map { mapToPresentation(it) }

    fun mapToEntity(item: CounterItem): CounterEntity = CounterEntity(
            item.title,
            item.typeDesc,
            item.counterValue,
            item.creationDate,
            item.reminderItem?.interval ?: 0,
            item.reminderItem?.time,
            item.reminderItem?.setReminder ?: false,
            item.id
    )
}