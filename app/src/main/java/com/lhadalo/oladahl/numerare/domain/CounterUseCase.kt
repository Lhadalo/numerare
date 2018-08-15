package com.lhadalo.oladahl.numerare.domain

import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.domain.model.CounterMapper
import com.lhadalo.oladahl.numerare.domain.model.ResetMapper
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class CounterUseCase @Inject constructor(
        private val repository: CounterRepository,
        private val resetMapper: ResetMapper,
        private val counterMapper: CounterMapper
) : CounterModel {

    override fun add(counter: CounterItem): Observable<Unit> {
        return Observable.fromCallable { repository.add(counterMapper.mapToEntity(counter)) }
                .flatMap { counterId ->
                    Observable.fromCallable { repository.addResetItem(ResetEntity(counterId, OffsetDateTime.now())) }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(counter: CounterItem) : Observable<Unit> {
        return Observable.fromCallable { repository.update(counterMapper.mapToEntity(counter)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateCount(id: Int, newValue: Int) {
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

    override fun get(id: Int): Flowable<CounterItem> {
        return repository.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { counterMapper.mapToPresentation(it) }
    }

    override fun addResetItem(item: ResetItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateResetItem(item: ResetItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCounterResetItems(id: Int): Flowable<List<ResetItem>> {
        return repository.getCounterResetItems(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { resetMapper.mapToPresentation(it) }
    }
}