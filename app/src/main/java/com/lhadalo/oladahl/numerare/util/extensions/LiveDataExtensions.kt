package com.lhadalo.oladahl.numerare.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T: Any, L: LiveData<T>> LifecycleOwner.observe(livedata: L, body: (T?) -> Unit) {
    livedata.observe(this, Observer(body))
}