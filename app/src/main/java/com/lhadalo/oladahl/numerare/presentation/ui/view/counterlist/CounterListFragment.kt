package com.lhadalo.oladahl.numerare.presentation.ui.view.counterlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.ui.activity.NavigationDelegate
import com.lhadalo.oladahl.numerare.presentation.ui.adapter.CounterListAdapter
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.extensions.observe
import com.lhadalo.oladahl.numerare.util.helpers.SwipeHandler
import com.lhadalo.oladahl.numerare.util.helpers.withViewModel
import kotlinx.android.synthetic.main.fragment_counter_list.*
import javax.inject.Inject

class CounterListFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    companion object {
        fun newInstance() = CounterListFragment()
    }

    private lateinit var navigator: NavigationDelegate

    private lateinit var viewModel: CounterListViewModel

    private val counterAdapter = CounterListAdapter { navigator.navigateToCounterDetailsFragment(it) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        getAppInjector().inject(this)

        return inflater.inflate(R.layout.fragment_counter_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)
        viewModel = withViewModel(factory) {
            observe(counters) {
                counters.value?.let {
                    counterAdapter.items = it
                }
            }
        }

        attachUI()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is NavigationDelegate) {
            navigator = context
        }
    }

    private fun attachUI() {
        //RecyclerView
        counters_recyclerview.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = counterAdapter
        }

        context?.let {
            val touchHelper = ItemTouchHelper(SwipeHandler(it) { action, pos ->
                when (action) {
                    SwipeHandler.LEFT_ACTION -> viewModel.onSwipeLeft(counterAdapter.getId(pos))
                    SwipeHandler.RIGHT_ACTION -> viewModel.onSwipeRight(counterAdapter.getId(pos))
                }
            })
            touchHelper.attachToRecyclerView(counters_recyclerview)
        }

        //FAB
        add_counter_fab.setOnClickListener { navigator.navigateToAddCounterFragment(null) }
    }
}
