package com.lhadalo.oladahl.numerare.domain

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import io.reactivex.Flowable
import io.reactivex.Single

interface CounterRepository {
    fun getAll(): Flowable<List<CounterEntity>>

    fun get(id: Long): Flowable<CounterEntity>

    fun add(counter: CounterEntity)

    fun update(counter: CounterEntity)

    fun updateCount(id: Long, newValue: Int)

    fun delete(counter: CounterEntity)

    fun addResetItem(resetEntity: ResetEntity)

    fun updateResetItem(resetEntity: ResetEntity)

    fun getCounterResetItems(id: Long): Flowable<List<ResetEntity>>

    fun getMostRecentResetItemWith(counterId: Long): ResetEntity

    fun getResetEntitySizeFor(counterId: Long): Int
}