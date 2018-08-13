package com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterMapper
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

//TODO Fundera om CounterItem kan vara observable
class CounterDetailViewModel @Inject constructor(private val model: CounterModel, private val mapper: CounterMapper) : ViewModel() {
    val counter = MutableLiveData<CounterItem>()

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
                .map { mapper.mapToItem(it) }
                .subscribe {
                    counter.postValue(it)
                })
    }

    fun onClickPlus() {
        val newVal = counter.value?.counterValue?.plus(1)
        if (newVal != null)
            updateValue(newVal)
    }


    fun onClickMinus() {
        val newValue = counter.value?.counterValue?.minus(1)
        if (newValue != null) {
            if (newValue >= 0) updateValue(newValue)
        }
    }

    //TODO Förstå hur threading fungerar och om jag ska göra någonting här (unsubscribe)
    private fun updateValue(newValue: Int) {
        counter.value?.let {
            Completable.fromAction { model.updateCount(it.id, newValue) }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        }
    }

    override fun onCleared() {
        _compositeDisposable.clear()
        super.onCleared()
    }
}