package com.lhadalo.oladahl.numerare.data.counter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "counter_table")
data class CounterEntity(
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "type")  val type: String,
        @ColumnInfo(name = "value") val value: Int,
        @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") val id: Int = 0
)