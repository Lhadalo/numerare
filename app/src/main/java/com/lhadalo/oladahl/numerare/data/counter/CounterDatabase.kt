package com.lhadalo.oladahl.numerare.data.counter

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lhadalo.oladahl.numerare.data.reset.DateTimeConverter
import com.lhadalo.oladahl.numerare.data.reset.ResetEntity

@Database(entities = [CounterEntity::class, ResetEntity::class], version = 8, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class CounterDatabase : RoomDatabase() {
    abstract fun counterDao() : CounterDao
}