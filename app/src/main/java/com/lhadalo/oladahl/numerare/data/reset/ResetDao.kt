package com.lhadalo.oladahl.numerare.data.reset

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import org.threeten.bp.OffsetDateTime

@Dao
interface ResetDao {
    @Insert
    fun insert(resetEntity: ResetEntity) : Long

    @Query("SELECT * FROM reset_table WHERE counter_id = :id")
    fun get(id: Long): Flowable<List<ResetEntity>>

    @Query("SELECT count(*) FROM reset_table where counter_id=:id")
    fun getSizeFor(id: Long): Int

    @Query("SELECT restore_date FROM reset_table WHERE id = (SELECT MAX(id) from reset_table WHERE counter_id=:id)")
    fun getLatestResetDate(id: Long): OffsetDateTime
}