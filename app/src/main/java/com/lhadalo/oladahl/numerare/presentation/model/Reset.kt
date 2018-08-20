package com.lhadalo.oladahl.numerare.presentation.model

import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import io.reactivex.Flowable
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

data class ResetItem(
        val counterId: Long,
        val initDate: OffsetDateTime?,
        val restoreDate: OffsetDateTime? = null,
        val counterValue: Int,
        val id: Long = 0
)

