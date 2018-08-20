package com.lhadalo.oladahl.numerare.data.counter

import androidx.room.*
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CounterDao {

    @Insert
    fun insert(counter: CounterEntity)

    @Query("SELECT * FROM counter_table ORDER BY id ASC")
    fun getAll(): Flowable<List<CounterEntity>>

    @Query("SELECT * FROM counter_table WHERE id=:id")
    fun getCounter(id: Long): Flowable<CounterEntity>

    @Update
    fun update(counter: CounterEntity)

    @Query("UPDATE counter_table SET value=:newValue where id=:id")
    fun updateCount(id: Long, newValue: Int)

    @Delete
    fun delete(counter: CounterEntity)

    @Insert
    fun insert(resetEntity: ResetEntity)

    @Update
    fun update(resetEntity: ResetEntity)

    @Query("SELECT * FROM reset_table WHERE counter_id=:counterId")
    fun getResetEntitiesFrom(counterId: Long): Flowable<List<ResetEntity>>

    @Query("SELECT * FROM reset_table WHERE id = (SELECT MAX(id) FROM reset_table WHERE counter_id=:counterId)")
    fun getMostRecentResetEntityFrom(counterId: Long): ResetEntity

    @Query("SELECT count(*) FROM reset_table where counter_id=:counterId")
    fun getEntitySize(counterId: Long): Int
}