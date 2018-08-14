package com.lhadalo.oladahl.numerare.data.counter

import androidx.room.*
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity
import io.reactivex.Flowable

@Dao
interface CounterDao {

    @Insert
    fun insert(counter: CounterEntity): Long

    @Query("SELECT * FROM counter_table ORDER BY id ASC")
    fun getAll(): Flowable<List<CounterEntity>>

    @Query("SELECT * FROM counter_table WHERE id=:id")
    fun getCounter(id: Int): Flowable<CounterEntity>

    @Update
    fun update(counter: CounterEntity)

    @Query("UPDATE counter_table SET value=:newValue where id=:id")
    fun updateCount(id: Int, newValue: Int)

    @Delete
    fun delete(counter: CounterEntity)

    @Insert
    fun insert(resetEntity: ResetEntity)

    @Update
    fun update(resetEntity: ResetEntity)

    @Query("SELECT * FROM reset_table WHERE counter_id=:id")
    fun getResetEntitiesFrom(id: Int): Flowable<List<ResetEntity>>
}