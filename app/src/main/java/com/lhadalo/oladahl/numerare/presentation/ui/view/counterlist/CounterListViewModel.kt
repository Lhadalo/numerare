package com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.data.CounterEntity
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CounterListViewModel @Inject constructor(private val model: CounterModel): ViewModel() {
    val counters = MutableLiveData<List<CounterEntity>>()

    private val compositeDisposable = CompositeDisposable()

    init {
        loadCounters()
    }

    private fun loadCounters() {
        compositeDisposable.add(model.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    counters.postValue(it)
                })
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
