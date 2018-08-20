package com.lhadalo.oladahl.numerare.data.counter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.threeten.bp.OffsetDateTime


@Entity(tableName = "counter_table")
data class CounterEntity(
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "type")  val type: String,
        @ColumnInfo(name = "value") val value: Int,
        @ColumnInfo(name = "creation_date") val creationDate: OffsetDateTime?,
        @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") val id: Long = 0
)