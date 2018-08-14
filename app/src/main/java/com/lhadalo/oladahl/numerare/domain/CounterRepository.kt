package com.lhadalo.oladahl.numerare.domain

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import io.reactivex.Flowable

interface CounterRepository {
    fun getAll(): Flowable<List<CounterEntity>>

    fun get(id: Int): Flowable<CounterEntity>

    fun add(counter: CounterEntity): Long

    fun update(counter: CounterEntity)

    fun updateCount(id: Int, newValue: Int)

    fun delete(counter: CounterEntity)

    fun addResetItem(resetEntity: ResetEntity)

    fun updateResetItem(resetEntity: ResetEntity)

    fun getCounterResetItems(id: Int): Flowable<List<ResetEntity>>
}