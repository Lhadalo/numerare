package com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.domain.CounterUseCase.Companion.DECREMENT
import com.lhadalo.oladahl.numerare.domain.CounterUseCase.Companion.INCREMENT
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class CounterDetailViewModel @Inject constructor(private val model: CounterModel) : ViewModel() {
    val counter = MutableLiveData<CounterItem>()

    companion object {
        const val TAG = "CounterDetailViewModel"
    }

    var counterId: Long? = null
        set(id) {
            if (field == null) {
                field = id
                id?.let { getCounter(id) }
            }
        }

    private val disposable = CompositeDisposable()


    private fun getCounter(id: Long) {
        disposable.add(model.get(id).subscribe {
            counter.postValue(it)
        })
        
    }

    fun onClickPlus() {
        //val newValue = counter.value?.counterValue?.plus(1)
        updateValue(1, INCREMENT)
    }


    fun onClickMinus() {
        val newValue = counter.value?.counterValue?.minus(1)
        if (newValue != null) {
            if (newValue >= 0) updateValue(1, DECREMENT)
        }
    }


    //TODO Förstå hur threading fungerar och om jag ska göra någonting här (unsubscribe)
    private fun updateValue(newValue: Int, operationType: Int) {
        counter.value?.let {
            model.updateCount(it.id, newValue, operationType)
        }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}