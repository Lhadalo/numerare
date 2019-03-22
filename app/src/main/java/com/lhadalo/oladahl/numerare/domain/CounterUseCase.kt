package com.lhadalo.oladahl.numerare.domain

import android.content.Context
import android.util.Log
import com.lhadalo.oladahl.numerare.domain.model.CounterMapper
import com.lhadalo.oladahl.numerare.domain.model.ResetMapper
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import javax.inject.Inject

class CounterUseCase @Inject constructor(
        private val context: Context,
        private val repository: CounterRepository,
        private val resetMapper: ResetMapper,
        private val counterMapper: CounterMapper
) : CounterModel {

    companion object {
        const val TAG = "CounterUseCase"
        const val INCREMENT = 1
        const val DECREMENT = 2
    }

    override fun add(counter: CounterItem) {
        Single.fromCallable { repository.add(counterMapper.mapToEntity(counter)) }
                .doAfterSuccess { id -> addReminder(counter, id) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun update(counter: CounterItem): Observable<Unit> {
        counter.reminderItem?.let { reminderItem ->
            if (reminderItem.setReminder) {
                NotificationHelper.createAlarm(context, counter.id, counter.title, reminderItem.interval, reminderItem.time)
            } else {
                NotificationHelper.cancelAlarm(context, counter.id, counter.title, reminderItem.interval, reminderItem.time)
            }
        }

        return Observable.fromCallable { repository.update(counterMapper.mapToEntity(counter)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateCount(id: Long, newValue: Int, operationType: Int) {
        Completable.fromAction {
            if (operationType == INCREMENT) {
                repository.increaseCounter(id, newValue)
            } else {
                repository.decreaseCounter(id, newValue)
            }
        }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun delete(counter: CounterItem): Observable<Unit> {
        return Observable.fromCallable { repository.delete(counterMapper.mapToEntity(counter)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAll(): Flowable<List<CounterItem>> {
        return repository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { counterMapper.mapToPresentation(it) }
    }

    override fun get(id: Long): Flowable<CounterItem> {
        return repository.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { counterMapper.mapToPresentation(it) }
    }

    override fun resetCounterWith(counterId: Long, counterValue: Int) {
        Single.fromCallable {repository.getResetEntitySizeFor(counterId) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doAfterSuccess { size ->
                    if (size > 0) { //If there are reset elements
                        val restoreDate = repository.getLatestRestoreDate(counterId)
                        repository.addResetItem(resetMapper.mapToEntity(ResetItem(counterId, restoreDate, OffsetDateTime.now(), counterValue)))
                    } else { //If counter newly created
                        val creationDate = repository.getLatestCreationDate(counterId)
                        repository.addResetItem(resetMapper.mapToEntity(ResetItem(counterId, creationDate, OffsetDateTime.now(), counterValue)))
                    }
                }.doAfterSuccess {
                    repository.reset(counterId) //Set value to zero
                }
                .subscribe()
    }

    override fun getCounterResetItems(id: Long): Flowable<List<ResetItem>> {
        return repository.getResetEntities(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { resetMapper.mapToPresentation(it) }
    }

    private fun addReminder(counter: CounterItem, counterId: Long) {
        counter.reminderItem?.let { reminderItem ->
            Log.d(TAG, reminderItem.toString())
            NotificationHelper.createAlarm(context, counterId, counter.title, reminderItem.interval, reminderItem.time)
        }
    }
}