package com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterMapper
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ObservableCounter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class CounterDetailViewModel @Inject constructor(private val model: CounterModel, private val mapper: CounterMapper) : ViewModel() {
    val counter = MutableLiveData<ObservableCounter>()
    var counterId: Int? = null
        set(id) {
            if (field == null) {
                field = id
                id?.let { getCounter(id) }
            }
        }

    private val _compositeDisposable = CompositeDisposable()

    private fun getCounter(id: Int) {
        _compositeDisposable.add(model.get(id)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { mapper.mapToPresentation(it) }
                .subscribe {
                    counter.postValue(it)
                })
    }

    fun onClickPlus() = updateValue(counter.value?.value?.get()?.plus(1))

    fun onClickMinus() = updateValue(counter.value?.value?.get()?.minus(1))

    //TODO Förstå hur threading fungerar och om jag ska göra någonting här (unsubscribe)
    private fun updateValue(newValue: Int?) {
        newValue?.let { value ->
            counterId?.let { id ->
                Completable.fromAction { model.updateCount(id, value) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
            }
        }
    }

    override fun onCleared() {
        Log.d("CounterDetailViewModel", _compositeDisposable.size().toString())
        _compositeDisposable.clear()
        super.onCleared()
    }
}