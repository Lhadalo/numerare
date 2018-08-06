package com.lhadalo.oladahl.numerare.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.view.adapter.CounterListAdapter
import com.lhadalo.oladahl.numerare.presentation.viewmodel.CounterListViewModel
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.helpers.withViewModel
import kotlinx.android.synthetic.main.fragment_counter_list.*
import javax.inject.Inject

class CounterListFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    companion object {
        fun newInstance() = CounterListFragment()
    }

    private lateinit var viewModel: CounterListViewModel

    private val adapter = CounterListAdapter {  }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        getAppInjector().inject(this)

        return inflater.inflate(R.layout.fragment_counter_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = withViewModel(factory) {

        }

        attachUI()
    }

    fun attachUI() {
        //RecyclerView
        counters_recyclerview.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        counters_recyclerview.adapter = adapter
    }

}
