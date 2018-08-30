package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import com.lhadalo.oladahl.numerare.util.AlarmReceiver
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AddCounterViewModel @Inject constructor(private val model: CounterModel, private val context: Context) : ViewModel() {
    private val disposable = CompositeDisposable()
    private fun currentViewState(): ViewState? = state.value

    var counter = CounterItem()
    val state: MutableLiveData<ViewState> = MutableLiveData()
    val result = MutableLiveData<Int>()

    companion object {
        const val TAG = "AddCounterViewModelTAG"
        const val SUCCESS = 1
        const val ERROR = 2
    }

    init {
        state.value = ViewState()
    }

    fun addCounter(title: String, typeDesc: String) {
        if (title.isNotEmpty()) {
            if (typeDesc.isNotEmpty()) {
                counter.typeDesc = typeDesc
            }
            counter.title = title
            disposable.add(model.add(counter).subscribe {
                result.postValue(SUCCESS)
            })

        } else {
            result.postValue(ERROR)
        }
    }

    fun deleteCounter() {
        disposable.add(model.delete(counter).subscribe {
            result.postValue(SUCCESS)
        })
    }

    fun updateCounter(title: String, typeDesc: String) {
        if (title.isNotEmpty()) {
            counter.title = title
            if (typeDesc.isNotEmpty()) counter.typeDesc = typeDesc
            disposable.add(model.update(counter).subscribe {
                result.postValue(SUCCESS)
            })
        } else {
            result.postValue(ERROR)
        }
    }

    fun onAddReminder(reminderItem: ReminderItem) {
        counter.reminderItem = reminderItem
    }

    fun checkLayoutMore() {
        currentViewState()?.let {
            state.value = it.copy(showMoreLayoutVisible = it.showMoreLayoutVisible.not())
        }
    }

    fun checkLayoutAuto() {
        currentViewState()?.let {
            state.value = it.copy(autoSwitchChecked = it.autoSwitchChecked.not())
        }
    }

    fun checkLayoutReset() {
        currentViewState()?.let {
            state.value = it.copy(resetSwitchChecked = it.resetSwitchChecked.not())
        }
    }

    fun setIsEditMode() {
        currentViewState()?.let {
            state.value = it.copy(inEditMode = true)
        }
    }

    fun isInEditMode(): Boolean {
        return currentViewState()?.inEditMode ?: false
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }


}