package com.lhadalo.oladahl.numerare.domain

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.OffsetDateTime


interface CounterRepository {
    fun getAll(): Flowable<List<CounterEntity>>

    fun get(id: Long): Flowable<CounterEntity>

    fun add(counter: CounterEntity): Long

    fun update(counter: CounterEntity)

    fun reset(id: Long)

    fun increaseCounter(id: Long, increaseBy: Int)

    fun decreaseCounter(id: Long, decreaseBy: Int)

    fun delete(counter: CounterEntity)

    fun addResetItem(resetEntity: ResetEntity)

    fun getResetEntities(id: Long): Flowable<List<ResetEntity>>

    fun getLatestCreationDate(id: Long) : OffsetDateTime

    fun getLatestRestoreDate(id: Long) : OffsetDateTime

    fun updateResetItem()

    fun getResetEntitySizeFor(counterId: Long): Int
}