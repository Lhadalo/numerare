package com.lhadalo.oladahl.numerare.domain.model

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import javax.inject.Inject

class CounterMapper @Inject constructor() {

    fun mapToPresentation(entity: CounterEntity): CounterItem {
        //TODO Göra snyggare lösning, ganska fult
        val reminderRepeatingDate: Int = entity.reminderRepeatingDate ?: 0
        val reminderSet: Boolean = entity.reminderIsSet ?: false

        val reminderItem = ReminderItem(reminderRepeatingDate, entity.reminderTime, reminderSet)

        return CounterItem(
                entity.id,
                entity.creationDate,
                entity.title,
                entity.type,
                entity.value,
                reminderItem
        )
    }

    fun mapToPresentation(counterList: List<CounterEntity>) = counterList.map { mapToPresentation(it) }

    fun mapToEntity(item: CounterItem): CounterEntity = CounterEntity(
            item.title,
            item.typeDesc,
            item.counterValue,
            item.creationDate,
            item.reminderItem?.repeatingDate,
            item.reminderItem?.time,
            item.reminderItem?.reminderSet,
            item.id
    )
}