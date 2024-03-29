package com.lhadalo.oladahl.numerare.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.*
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterFragment
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail.CounterDetailFragment
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterhistory.CounterHistoryFragment
import com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist.CounterListFragment
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper

interface NavigationDelegate {

    fun navigateToAddCounterFragment(counterItem: CounterItem?)

    fun popBackStack()

    fun navigateToCounterListFragment()

    fun navigateToCounterDetailsFragment(counterId: Long)

    fun navigateToHistory(counterId: Long, counterValue: Int)
}

class MainActivity : AppCompatActivity(), NavigationDelegate {

    companion object {
        private const val TAG = "MainActivityTAG"
        private const val MOVE_DEFAULT_TIME: Long = 500
        private const val COUNTER_LIST_FRAGMENT_TAG = "counter_list_fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        val counterId = intent.getLongExtra(NotificationHelper.COUNTER_ID, -1)
        if (counterId > -1) {
            Log.d(TAG, "Launch counter detail")
        } else {
            Log.d(TAG, "Proceed as usual")
        }

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CounterListFragment.newInstance())
                    .commitNow()
        }
    }

    private fun setup() {
        NotificationHelper.setupNotificationChannels(this)
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
                .addToBackStack(COUNTER_LIST_FRAGMENT_TAG)
                .commit()
    }


    override fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    override fun navigateToCounterDetailsFragment(counterId: Long) {
        val newFragment = CounterDetailFragment.newInstance(counterId)

        newFragment.allowEnterTransitionOverlap = false
        newFragment.allowReturnTransitionOverlap = false


        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment != null) {

        }

        newFragment.enterTransition = getSlideTransition()
        newFragment.allowEnterTransitionOverlap = false

        supportFragmentManager.beginTransaction()
                .add(R.id.container, newFragment)
                .addToBackStack(COUNTER_LIST_FRAGMENT_TAG)
                .commit()
    }

    override fun navigateToHistory(counterId: Long, counterValue: Int) {
        val newFragment = CounterHistoryFragment.newInstance(counterId, counterValue)

        newFragment.enterTransition = getSlideTransition()

        supportFragmentManager.beginTransaction()
                .add(R.id.container, newFragment)
                .addToBackStack(null)
                .commit()
    }

    override fun navigateToCounterListFragment() {
        supportFragmentManager.popBackStack(COUNTER_LIST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun getSlideTransition() : Transition {
        val slideTransition = Slide()
        slideTransition.slideEdge = Gravity.BOTTOM
        slideTransition.duration = MOVE_DEFAULT_TIME
        slideTransition.interpolator = DecelerateInterpolator(3F)

        return slideTransition
    }


}
