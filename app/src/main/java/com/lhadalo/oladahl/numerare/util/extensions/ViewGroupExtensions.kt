package com.lhadalo.oladahl.numerare.util.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.databinding.ListItemCountersBinding

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.bind(layoutRes: Int): ViewDataBinding {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, false)
}