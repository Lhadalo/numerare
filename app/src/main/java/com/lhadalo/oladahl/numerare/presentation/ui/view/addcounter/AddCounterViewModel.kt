package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterMapper
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddCounterViewModel @Inject constructor(private val model: CounterModel, private val mapper: CounterMapper) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private fun currentViewState(): ViewState? = state.value

    var counter = CounterItem()
    val state: MutableLiveData<ViewState> = MutableLiveData()
    val result = MutableLiveData<Int>()

    companion object {
        const val SUCCESS = 1
        const val UPDATE_DELETE_SUCCESS = 2
        const val ERROR = 3
    }

    init {
        state.value = ViewState()
    }

    fun addCounter(title: String, typeDesc: String) {
        if (title.isNotEmpty()) {
            counter.title = title
            if (typeDesc.isNotEmpty()) counter.typeDesc = typeDesc
            compositeDisposable.add(Completable.fromAction { model.add(mapper.mapToEnitity(counter)) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        result.postValue(SUCCESS)
                    }
            )
        } else {
            result.postValue(ERROR)
        }
    }

    fun deleteCounter() {
        compositeDisposable.add(Completable.fromAction { model.delete(mapper.mapToEnitity(counter)) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            result.postValue(SUCCESS)
                        }
        )
    }

    fun updateCounter(title: String, typeDesc: String) {
        if (title.isNotEmpty()) {
            counter.title = title
            if (typeDesc.isNotEmpty()) counter.typeDesc = typeDesc

            compositeDisposable.add(Completable.fromAction { model.update(mapper.mapToEnitity(counter)) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        result.postValue(SUCCESS)
                    }
            )
        } else {
            result.postValue(ERROR)
        }
    }

    fun onAddReminder(reminderItem: ReminderItem) {

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
            state.value = it.copy(editMode = true)
        }
    }

    fun isEditMode() : Boolean {
        return currentViewState()?.editMode ?: false
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


}