package com.lhadalo.oladahl.numerare.presentation.model

import com.lhadalo.oladahl.numerare.data.CounterEntity
import io.reactivex.Flowable

interface CounterModel {
    fun getAll() : Flowable<List<CounterEntity>>

    fun get(id: Int) : Flowable<CounterEntity>

    fun add(counter: CounterEntity)

    fun update(counter: CounterEntity)

    fun updateCount(id: Int, newValue: Int)

    fun delete(counter: CounterEntity)
}