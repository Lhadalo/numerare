package com.lhadalo.oladahl.numerare.util.di.modules

import android.content.Context
import com.lhadalo.oladahl.numerare.data.RoomCounterRepository
import com.lhadalo.oladahl.numerare.domain.CounterRepository
import com.lhadalo.oladahl.numerare.domain.CounterUseCase
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRoomCounterRepository(repository: RoomCounterRepository) : CounterRepository

    @Binds
    abstract fun bindModelWithUseCase(useCase: CounterUseCase) : CounterModel

}