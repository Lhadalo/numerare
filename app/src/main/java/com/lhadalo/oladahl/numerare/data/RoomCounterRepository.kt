package com.lhadalo.oladahl.numerare.data

import com.lhadalo.oladahl.numerare.domain.CounterRepository
import io.reactivex.Flowable
import javax.inject.Inject

class RoomCounterRepository @Inject constructor(val dao: CounterDao) : CounterRepository {

    override fun getAll(): Flowable<List<Counter>> = dao.getAll()

    override fun get(id: Int): Flowable<Counter> = dao.getCounter(id)

    override fun add(counter: Counter) = dao.insert(counter)

    override fun update(counter: Counter) = dao.update(counter)

    override fun delete(counter: Counter) = dao.delete(counter)

    override fun updateCount(id: Int, newValue: Int) = dao.updateCount(id, newValue)
}