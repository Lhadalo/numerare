package com.lhadalo.oladahl.numerare.presentation.ui.view.counterhistory

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.ui.activity.NavigationDelegate
import com.lhadalo.oladahl.numerare.presentation.ui.adapter.HistoryListAdapter
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.extensions.observe
import com.lhadalo.oladahl.numerare.util.helpers.withViewModel
import kotlinx.android.synthetic.main.fragment_counter_history.*
import javax.inject.Inject

class CounterHistoryFragment : Fragment() {

    companion object {
        const val COUNTER_ID = "counter_id"
        const val COUNTER_VALUE = "counter_value"

        fun newInstance(counterId: Long, counterValue: Int) = CounterHistoryFragment().apply {
            arguments = Bundle().apply {
                putLong(COUNTER_ID, counterId)
                putInt(COUNTER_VALUE, counterValue)
            }
        }
    }

    private val historyAdapter = HistoryListAdapter()

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: CounterHistoryViewModel
    private lateinit var navigator: NavigationDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getAppInjector().inject(this)

        viewModel = withViewModel(factory) {
            counterId = arguments?.getLong(COUNTER_ID)
            counterValue = arguments?.getInt(COUNTER_VALUE)

            observe(history, historyAdapter::addItems)
            observe(totalCount, ::setTotalCount)
        }

        return inflater.inflate(R.layout.fragment_counter_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appCompatActivity = (activity as AppCompatActivity)
        appCompatActivity.setSupportActionBar(history_toolbar)
        appCompatActivity.title = "History"

        history_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = historyAdapter
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }

        btn_reset_counter.setOnClickListener { viewModel.resetCounter() }
    }

    private fun setTotalCount(totalCount: Int?) {
        tv_value_total.text = totalCount.toString()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is NavigationDelegate) {
            navigator = context
        }
    }
}