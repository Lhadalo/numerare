package com.lhadalo.oladahl.numerare.data

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface CounterDao {

    @Insert
    fun insert(counter: Counter)

    @Query("SELECT * FROM counter_table ORDER BY id ASC")
    fun getAll() : Flowable<List<Counter>>

    @Query("SELECT * FROM counter_table WHERE id=:id")
    fun getCounter(id: Int) : Flowable<Counter>

    @Update
    fun update(counter: Counter)

    @Query("UPDATE counter_table SET value=:newValue where id=:id")
    fun updateCount(id: Int, newValue: Int)

    @Delete
    fun delete(counter: Counter)
}