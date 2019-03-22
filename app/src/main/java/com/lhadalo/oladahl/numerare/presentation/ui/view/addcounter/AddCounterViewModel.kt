package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ReminderItem
import com.lhadalo.oladahl.numerare.util.helpers.DAY
import com.lhadalo.oladahl.numerare.util.helpers.MONTH
import com.lhadalo.oladahl.numerare.util.helpers.WEEK
import io.reactivex.disposables.CompositeDisposable
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
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

    fun addCounter(title: String) {
        if (title.isNotEmpty()) {
            counter.title = title
            model.add(counter)
            result.postValue(SUCCESS) //Faran är att den inte hinner lägga till
        } else {
            result.postValue(ERROR)
        }
    }

    fun deleteCounter() {
        disposable.add(model.delete(counter).subscribe {
            result.postValue(SUCCESS)
        })
    }

    fun mapToViewState(counter: CounterItem) {
        counter.reminderItem?.let { item ->
            currentViewState()?.let { current->
                state.value = current.copy(
                        hasReminder = true,
                        reminderText = formatReminderText(item)
                )
            }
        }
    }

    fun updateCounter(title: String) {
        if (title.isNotEmpty()) {
            counter.title = title
            disposable.add(model.update(counter).subscribe {
                result.postValue(SUCCESS)
            })
        } else {
            result.postValue(ERROR)
        }
    }

    fun onAddReminder(reminderItem: ReminderItem) {
        counter.reminderItem = reminderItem
        currentViewState()?.let {
            state.value = it.copy(
                    hasReminder = true,
                    reminderText = formatReminderText(reminderItem)
            )
        }
    }

    fun clearReminder() {
        counter.reminderItem = counter.reminderItem?.copy(setReminder = false)
        currentViewState()?.let {
            state.value = it.copy(
                    hasReminder = false,
                    reminderText = context.getString(R.string.title_reminder)
            )
        }
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

    fun hasReminder(): Boolean {
        return currentViewState()?.hasReminder ?: false
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    private fun formatReminderText(reminderItem: ReminderItem): String {
        val stringBuilder = StringBuilder()
        val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

        reminderItem.time?.let {
            stringBuilder.append(it.format(timeFormatter))
                    .append(' ')
                    .append(context.resources.getText(R.string.title_reminder_every))
                    .append(' ')
                    .append(getRepeatingDateString(reminderItem.interval))
        }


        return stringBuilder.toString()
    }

    private fun getRepeatingDateString(dateType: Int): CharSequence {
        return when (dateType) {
            DAY -> context.resources.getText(R.string.title_reminder_day)
            WEEK -> context.resources.getText(R.string.title_reminder_week)
            MONTH -> context.resources.getText(R.string.title_reminder_month)
            else -> "Error"
        }
    }
}