package com.lhadalo.oladahl.numerare.util.extensions

import androidx.fragment.app.Fragment
import com.lhadalo.oladahl.numerare.App
import com.lhadalo.oladahl.numerare.util.di.Injector

val Fragment.app: App get() = activity!!.application as App

fun Fragment.getAppInjector() : Injector = (app).injector