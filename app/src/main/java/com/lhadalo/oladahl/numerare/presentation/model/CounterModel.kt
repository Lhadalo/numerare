package com.lhadalo.oladahl.numerare.presentation.model

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface CounterModel {
    fun getAll() : Flowable<List<CounterItem>>

    fun get(id: Long) : Flowable<CounterItem>

    fun add(counter: CounterItem)

    fun update(counter: CounterItem): Observable<Unit>

    fun updateCount(id: Long, newValue: Int, operationType: Int)

    fun delete(counter: CounterItem): Observable<Unit>

    fun resetCounterWith(counterId: Long, counterValue: Int)

    fun getCounterResetItems(id: Long): Flowable<List<ResetItem>>
}