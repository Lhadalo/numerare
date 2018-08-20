package com.lhadalo.oladahl.numerare.data

import com.lhadalo.oladahl.numerare.data.counter.CounterDao
import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.domain.CounterRepository
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class RoomCounterRepository @Inject constructor(private val dao: CounterDao) : CounterRepository {

    override fun getAll(): Flowable<List<CounterEntity>> = dao.getAll()

    override fun get(id: Long): Flowable<CounterEntity> = dao.getCounter(id)

    override fun add(counter: CounterEntity) = dao.insert(counter)

    override fun update(counter: CounterEntity) = dao.update(counter)

    override fun delete(counter: CounterEntity) = dao.delete(counter)

    override fun updateCount(id: Long, newValue: Int) = dao.updateCount(id, newValue)

    override fun addResetItem(resetEntity: ResetEntity) = dao.insert(resetEntity)

    override fun updateResetItem(resetEntity: ResetEntity) = dao.update(resetEntity)

    override fun getCounterResetItems(id: Long): Flowable<List<ResetEntity>> = dao.getResetEntitiesFrom(id)

    override fun getMostRecentResetItemWith(counterId: Long): ResetEntity = dao.getMostRecentResetEntityFrom(counterId)

    override fun getResetEntitySizeFor(counterId: Long): Int = dao.getEntitySize(counterId)
}