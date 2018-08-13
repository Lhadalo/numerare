package com.lhadalo.oladahl.numerare.util.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterViewModel
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail.CounterDetailViewModel
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist.CounterListViewModel
import com.lhadalo.oladahl.numerare.util.helpers.ViewModelFactory
import com.lhadalo.oladahl.numerare.util.helpers.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CounterListViewModel::class)
    internal abstract fun counterListViewModel(viewModel: CounterListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddCounterViewModel::class)
    internal abstract fun addCounterViewModel(viewModel: AddCounterViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CounterDetailViewModel::class)
    internal abstract fun counterDetailsViewModel(viewModel: CounterDetailViewModel) : ViewModel
}