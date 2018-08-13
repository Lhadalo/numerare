package com.lhadalo.oladahl.numerare.data

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
}