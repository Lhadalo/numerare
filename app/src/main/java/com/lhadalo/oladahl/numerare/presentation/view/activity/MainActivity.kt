package com.lhadalo.oladahl.numerare.presentation.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.view.CounterListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CounterListFragment.newInstance())
                    .commitNow()
        }
    }

}
