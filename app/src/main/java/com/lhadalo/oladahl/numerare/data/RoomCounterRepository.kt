package com.lhadalo.oladahl.numerare.data

import android.util.Log
import com.lhadalo.oladahl.numerare.data.counter.CounterDao
import com.lhadalo.oladahl.numerare.data.counter.CounterEntity
import com.lhadalo.oladahl.numerare.data.reset.ResetDao
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import com.lhadalo.oladahl.numerare.domain.CounterRepository
import io.reactivex.Flowable
import org.threeten.bp.OffsetDateTime

import javax.inject.Inject

class RoomCounterRepository @Inject constructor(private val dao: CounterDao, private val resetDao: ResetDao) : CounterRepository {
    override fun getAll(): Flowable<List<CounterEntity>> = dao.getAll()

    override fun get(id: Long): Flowable<CounterEntity> = dao.getCounter(id)

    override fun add(counter: CounterEntity) = dao.insert(counter)

    override fun update(counter: CounterEntity) = dao.update(counter)

    override fun delete(counter: CounterEntity) = dao.delete(counter)

    override fun reset(id: Long) = dao.reset(id)

    override fun increaseCounter(id: Long, increaseBy: Int) = dao.increaseCount(id, increaseBy)

    override fun decreaseCounter(id: Long, decreaseBy: Int) = dao.decreaseCount(id, decreaseBy)

    override fun addResetItem(resetEntity: ResetEntity) {
        resetDao.insert(resetEntity)
    }

    override fun getResetEntities(id: Long): Flowable<List<ResetEntity>> {
        return resetDao.get(id)
    }

    override fun getLatestCreationDate(id: Long): OffsetDateTime {
        return dao.getLatestCreationDate(id)
    }

    override fun getLatestRestoreDate(id: Long): OffsetDateTime {
        return resetDao.getLatestResetDate(id)
    }

    override fun updateResetItem() {
        Log.d("RoomCounterRepository", "updateResetItem")
    }

    override fun getResetEntitySizeFor(counterId: Long): Int = resetDao.getSizeFor(counterId)
}