package com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CounterListViewModel @Inject constructor(private val model: CounterModel): ViewModel() {
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

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
