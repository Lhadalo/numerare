package com.lhadalo.oladahl.numerare.util.di.modules

import android.content.Context
import androidx.room.Room
import com.lhadalo.oladahl.numerare.data.CounterDatabase
import com.lhadalo.oladahl.numerare.presentation.model.ObservableCounter
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddReminderDialog
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context = context

    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): CounterDatabase =
            Room.databaseBuilder(context, CounterDatabase::class.java, "counter_database").build()

    @Provides
    @Singleton
    fun providesCounterDao(database: CounterDatabase) = database.counterDao()

    @Provides
    @Singleton
    fun provideObservableCounter() = ObservableCounter()

}