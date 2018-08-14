package com.lhadalo.oladahl.numerare.data.reset

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "reset_table")
data class ResetEntity(
        @ColumnInfo(name = "counter_id") val counterId: Long,
        @ColumnInfo(name = "init_date") val initDate: OffsetDateTime? = null,
        @ColumnInfo(name = "restore_date") val restoreDate: OffsetDateTime? = null,
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0
)