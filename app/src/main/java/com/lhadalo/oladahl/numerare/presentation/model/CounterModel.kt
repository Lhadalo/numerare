package com.lhadalo.oladahl.numerare.presentation.model

import com.lhadalo.oladahl.numerare.data.Counter
import io.reactivex.Flowable

interface CounterModel {
    fun getAll() : Flowable<List<Counter>>

    fun get(id: Int) : Flowable<Counter>

    fun add(counter: Counter)

    fun update(counter: Counter)

    fun updateCount(id: Int, newValue: Int)

    fun delete(counter: Counter)
}