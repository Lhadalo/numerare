package com.lhadalo.oladahl.numerare.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Counter::class], version = 1, exportSchema = false)
abstract class CounterDatabase : RoomDatabase() {
    abstract fun counterDao() : CounterDao
}