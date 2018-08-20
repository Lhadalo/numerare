package com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CounterListViewModel @Inject constructor(private val model: CounterModel) : ViewModel() {
    val counters = MutableLiveData<List<CounterItem>>()

    private val compositeDisposable = CompositeDisposable()

    init {
        loadCounters()
    }

    private fun loadCounters() {
        compositeDisposable.add(model.getAll().subscribe {
            counters.postValue(it)
        })
    }

    fun onSwipeRight(idAndValue: Pair<Long, Int>) {
        val (id, value) = idAndValue
        updateValue(id, value.plus(1))
    }

    fun onSwipeLeft(idAndValue: Pair<Long, Int>) {
        val (id, value) = idAndValue
        updateValue(id, value.minus(1))
    }

    private fun updateValue(id: Long, value: Int) {
        if (value >= 0) {
            model.updateCount(id, value)
        }
    }

    //4322
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
