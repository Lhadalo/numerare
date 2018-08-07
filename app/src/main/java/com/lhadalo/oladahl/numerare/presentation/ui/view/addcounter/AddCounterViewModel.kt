package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.data.Counter
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddCounterViewModel @Inject constructor(private val model: CounterModel) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun addCounter(title: String, type: String, value: Int) {
        val counter = Counter(title, type, value)

        compositeDisposable.add(Completable.fromAction { model.add(counter) }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}