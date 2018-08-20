package com.lhadalo.oladahl.numerare.presentation.ui.view.counterhistory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CounterHistoryViewModel @Inject constructor(private val model: CounterModel) : ViewModel() {

    companion object {
        const val TAG = "CounterHistoryViewModel"
    }

    val history = MutableLiveData<List<ResetItem>>()
    val totalCount = MutableLiveData<Int>()
    private val disposable = CompositeDisposable()

    var counterValue: Int? = null
    var counterId: Long? = null
        set(id) {
            if (field == null) {
                field = id
                id?.let { getHistory(id) }
            }
        }

    private fun getHistory(counterId: Long) {
        disposable.add(model.getCounterResetItems(counterId).subscribe {
            history.postValue(it)
            val sum = it.sumBy { resetItem ->  resetItem.counterValue }
            totalCount.postValue(sum)
        })
    }

    fun resetCounter() {
        counterId?.let { id ->
            counterValue?.let { value ->
                if (value > 0) {
                    model.resetCounterWith(id, value)
                }
            }
        }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}