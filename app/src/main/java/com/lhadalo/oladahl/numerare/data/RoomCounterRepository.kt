package com.lhadalo.oladahl.numerare.data

import com.lhadalo.oladahl.numerare.data.counter.CounterDao
import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.domain.CounterRepository
import io.reactivex.Flowable
import javax.inject.Inject

class RoomCounterRepository @Inject constructor(val dao: CounterDao) : CounterRepository {

    override fun getAll(): Flowable<List<CounterEntity>> = dao.getAll()

    override fun get(id: Int): Flowable<CounterEntity> = dao.getCounter(id)

    override fun add(counter: CounterEntity) = dao.insert(counter)

    override fun update(counter: CounterEntity) = dao.update(counter)

    override fun delete(counter: CounterEntity) = dao.delete(counter)

    override fun updateCount(id: Int, newValue: Int) = dao.updateCount(id, newValue)

    override fun addResetItem(resetEntity: ResetEntity) = dao.insert(resetEntity)

    override fun updateResetItem(resetEntity: ResetEntity) = dao.update(resetEntity)

    override fun getCounterResetItems(id: Int): Flowable<List<ResetEntity>> = dao.getResetEntitiesFrom(id)
}