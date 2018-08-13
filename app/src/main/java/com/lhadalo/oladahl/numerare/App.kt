package com.lhadalo.oladahl.numerare

import android.app.Application
import com.lhadalo.oladahl.numerare.util.di.DaggerInjector
import com.lhadalo.oladahl.numerare.util.di.Injector
import com.lhadalo.oladahl.numerare.util.di.modules.AppModule

class App : Application() {
    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        injector = DaggerInjector.builder()
                .appModule(AppModule(this))
                .build()
    }
}