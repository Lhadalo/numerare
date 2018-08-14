package com.lhadalo.oladahl.numerare.presentation.model

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

interface CounterModel {
    fun getAll() : Flowable<List<CounterItem>>

    fun get(id: Int) : Flowable<CounterItem>

    fun add(counter: CounterItem): Observable<Unit>

    fun update(counter: CounterItem): Observable<Unit>

    fun updateCount(id: Int, newValue: Int)

    fun delete(counter: CounterItem): Observable<Unit>

    fun addResetItem(item: ResetItem)

    fun updateResetItem(item: ResetItem)

    fun getCounterResetItems(id: Int): Flowable<List<ResetItem>>
}