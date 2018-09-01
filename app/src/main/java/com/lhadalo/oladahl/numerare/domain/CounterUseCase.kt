package com.lhadalo.oladahl.numerare.domain

import android.content.Context
import android.util.Log
import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.domain.model.CounterMapper
import com.lhadalo.oladahl.numerare.domain.model.ResetMapper
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import com.lhadalo.oladahl.numerare.util.AlarmReceiver
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
    }

    override fun add(counter: CounterItem): Observable<Unit> {
        return Observable.fromCallable {
            counter.creationDate = OffsetDateTime.now(ZoneId.systemDefault())
            repository.add(counterMapper.mapToEntity(counter))
        }
                .flatMap { id ->
                    Observable.fromCallable { addReminder(counter, id) }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(counter: CounterItem): Observable<Unit> {
        counter.reminderItem?.let { reminderItem ->
            if (reminderItem.setReminder) {
                Log.d(TAG, "Update Reminder")
                NotificationHelper.createAlarm(context, counter.id, counter.title, reminderItem.interval, reminderItem.time)
            } else {
                Log.d(TAG, "Cancel Reminder")
                NotificationHelper.cancelAlarm(context, counter.id, counter.title, reminderItem.interval, reminderItem.time)
            }
        }

        return Observable.fromCallable { repository.update(counterMapper.mapToEntity(counter)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateCount(id: Long, newValue: Int) {
        Completable.fromAction { repository.updateCount(id, newValue) }
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

    override fun resetCounterWith(counterId: Long, counterValue: Int): Disposable {
        return Single.fromCallable { repository.getResetEntitySizeFor(counterId) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { size ->
                    if (size == 0) {
                        repository.get(counterId).firstOrError().subscribe { counter: CounterEntity?, _: Throwable? ->
                            counter?.let {
                                Completable.fromCallable {
                                    repository.addResetItem(ResetEntity(counterId, it.creationDate, OffsetDateTime.now(), counterValue))
                                }.subscribe {
                                    repository.updateCount(counterId, 0)
                                }
                            }
                        }
                    } else {
                        Single.fromCallable { repository.getMostRecentResetItemWith(counterId) }
                                .subscribe { entity: ResetEntity?, _: Throwable? ->
                                    entity?.let {
                                        Completable.fromCallable {
                                            repository.addResetItem(ResetEntity(counterId, it.restoreDate, OffsetDateTime.now(), counterValue))
                                        }.subscribe {
                                            repository.updateCount(counterId, 0)
                                        }
                                    }
                                }
                    }
                }
    }

    override fun updateResetItem(item: ResetItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCounterResetItems(id: Long): Flowable<List<ResetItem>> {
        return repository.getCounterResetItems(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { resetMapper.mapToPresentation(it) }
    }

    private fun addReminder(counter: CounterItem, counterId: Long) {
        counter.reminderItem?.let { reminderItem ->
            Log.d(TAG, "Create Reminder")
            NotificationHelper.createAlarm(context, counterId, counter.title, reminderItem.interval, reminderItem.time)
        }
    }
}