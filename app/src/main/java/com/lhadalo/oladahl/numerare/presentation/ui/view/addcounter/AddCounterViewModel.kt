package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterMapper
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddCounterViewModel @Inject constructor(private val model: CounterModel, private val mapper: CounterMapper) : ViewModel() {
    var counterItem = CounterItem()
    val liveCounter = MutableLiveData<CounterItem>()

    private val compositeDisposable = CompositeDisposable()

    fun addCounter(title: String, type: String) {
        counterItem.title = title
        counterItem.typeDesc = type

        compositeDisposable.add(Completable.fromAction { model.add(mapper.mapToEnitity(counterItem)) }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    fun onAddReminder(reminderItem: ReminderItem) {

    }

    fun deleteCounter() {

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}