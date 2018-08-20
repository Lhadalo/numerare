package com.lhadalo.oladahl.numerare.domain.model

import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import javax.inject.Inject

class ResetMapper @Inject constructor() {

    fun mapToEntity(item: ResetItem): ResetEntity = ResetEntity(
            item.counterId,
            item.initDate,
            item.restoreDate,
            item.counterValue,
            item.id
    )

    fun mapToPresentation(entity: ResetEntity): ResetItem = ResetItem(
            entity.counterId,
            entity.initDate,
            entity.restoreDate,
            entity.counterValue,
            entity.id
    )

    fun mapToPresentation(list: List<ResetEntity>) = list.map { mapToPresentation(it) }
}