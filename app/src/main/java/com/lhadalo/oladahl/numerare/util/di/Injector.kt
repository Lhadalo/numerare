package com.lhadalo.oladahl.numerare.util.di

import com.lhadalo.oladahl.numerare.util.di.modules.AppModule
import com.lhadalo.oladahl.numerare.presentation.view.CounterListFragment
import com.lhadalo.oladahl.numerare.util.di.modules.RepositoryModule
import com.lhadalo.oladahl.numerare.util.di.modules.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RepositoryModule::class])
interface Injector {
    fun inject(fragment: CounterListFragment)
}