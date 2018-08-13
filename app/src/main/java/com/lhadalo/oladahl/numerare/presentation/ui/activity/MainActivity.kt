package com.lhadalo.oladahl.numerare.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import androidx.transition.Slide
import androidx.transition.Transition
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterFragment
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail.CounterDetailFragment
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist.CounterListFragment

interface NavigationDelegate {

    fun navigateToAddCounterFragment(counterItem: CounterItem?)

    fun popBackStack()

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

    //TODO Göra en snyggare lösning än att kolla efter null
    override fun navigateToAddCounterFragment(counterItem: CounterItem?) {

        val newFragment = when (counterItem == null) {
            true -> AddCounterFragment.newInstance()
            else -> AddCounterFragment.newInstance(counterItem)
        }

        newFragment.enterTransition = getSlideTransition()

        supportFragmentManager.beginTransaction()
                .add(R.id.container, newFragment)
                .addToBackStack(null)
                .commit()
    }


    override fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    override fun navigateToCounterDetailsFragment(id: Int) {
        val newFragment = CounterDetailFragment.newInstance(id)

        newFragment.enterTransition = getSlideTransition()

        supportFragmentManager.beginTransaction()
                .add(R.id.container, newFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun getSlideTransition() : Transition {
        val slideTransition = Slide()
        slideTransition.slideEdge = Gravity.BOTTOM
        slideTransition.duration = MOVE_DEFAULT_TIME
        slideTransition.interpolator = DecelerateInterpolator(3F)

        return slideTransition
    }
}
