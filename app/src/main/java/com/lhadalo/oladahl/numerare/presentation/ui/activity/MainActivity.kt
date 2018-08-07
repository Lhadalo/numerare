package com.lhadalo.oladahl.numerare.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import androidx.transition.Slide
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterFragment
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail.CounterDetailFragment
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist.CounterListFragment

interface NavigationDelegate {
    fun navigateToAddCounterFragment()

    fun navigateToCounterListFragment()

    fun navigateToCounterDetailsFragment(id: Int)
}

class MainActivity : AppCompatActivity(), NavigationDelegate {

    companion object {
        private const val MOVE_DEFAULT_TIME: Long = 500
        private const val COUNTER_LIST_FRAGMENT_TAG = "counter_list_fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CounterListFragment.newInstance())
                    .commitNow()
        }
    }

    override fun navigateToAddCounterFragment() {
        val newFragment = AddCounterFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()

        val slideTransition = Slide()
        slideTransition.slideEdge = Gravity.BOTTOM
        slideTransition.duration = MOVE_DEFAULT_TIME
        slideTransition.interpolator = DecelerateInterpolator(3F)

        newFragment.enterTransition = slideTransition

        transaction.addToBackStack(null)
        transaction.add(R.id.container, newFragment)
        transaction.commit()
    }

    override fun navigateToCounterListFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun navigateToCounterDetailsFragment(id: Int) {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, CounterDetailFragment.newInstance(id))
                .addToBackStack(null)
                .commit()
    }
}
