package com.lhadalo.oladahl.numerare.data.counter

import androidx.room.*
import io.reactivex.Flowable
import org.threeten.bp.OffsetDateTime

@Dao
interface CounterDao {

    @Insert
    fun insert(counter: CounterEntity): Long

    @Query("SELECT * FROM counter_table ORDER BY id ASC")
    fun getAll(): Flowable<List<CounterEntity>>

    @Query("SELECT * FROM counter_table WHERE id=:id")
    fun getCounter(id: Long): Flowable<CounterEntity>

    @Update
    fun update(counter: CounterEntity)

    @Query("UPDATE counter_table SET value= value + :newValue where id=:id")
    fun increaseCount(id: Long, newValue: Int)

    @Query("UPDATE counter_table SET value= value - :newValue where id=:id")
    fun decreaseCount(id: Long, newValue: Int)

    @Query("UPDATE counter_table SET value = 0 WHERE id=:id")
    fun reset(id: Long)

    @Query("SELECT creation_date FROM counter_table WHERE id = (SELECT MAX(id) from counter_table WHERE id=:id)")
    fun getLatestCreationDate(id: Long): OffsetDateTime

    @Delete
    fun delete(counter: CounterEntity)

    @Query("SELECT count(*) FROM reset_table where counter_id=:counterId")
    fun getEntitySize(counterId: Long): Int
}