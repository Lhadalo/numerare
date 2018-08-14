package com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

//TODO Fundera om CounterItem kan vara observable
class CounterDetailViewModel @Inject constructor(private val model: CounterModel) : ViewModel() {
    val counter = MutableLiveData<CounterItem>()

    companion object {
        const val TAG = "CounterDetailViewModel"
    }

    var counterId: Int? = null
        set(id) {
            if (field == null) {
                field = id
                id?.let { getCounter(id) }
            }
        }

    private val disposable = CompositeDisposable()


    private fun getCounter(id: Int) {
        disposable.add(model.get(id).subscribe {
            counter.postValue(it)
        })
    }

    fun onClickPlus() {
        val newValue = counter.value?.counterValue?.plus(1)
        if (newValue != null)
            updateValue(newValue)
    }


    fun onClickMinus() {
        val newValue = counter.value?.counterValue?.minus(1)
        if (newValue != null) {
            if (newValue >= 0) updateValue(newValue)
        }
    }

    fun restoreValue() {
        counter.value?.let {
            disposable.add(model.getCounterResetItems(it.id)
                    .subscribe {
                        it.forEach {
                            Log.d(TAG, it.toString())
                        }
                    }
            )
        }
    }

    //TODO Förstå hur threading fungerar och om jag ska göra någonting här (unsubscribe)
    private fun updateValue(newValue: Int) {
        counter.value?.let {
            model.updateCount(it.id, newValue)
        }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}