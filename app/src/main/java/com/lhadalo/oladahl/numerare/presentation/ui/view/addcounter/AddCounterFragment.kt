package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.ui.activity.NavigationDelegate
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.helpers.getViewModel
import kotlinx.android.synthetic.main.fragment_add_counter.*
import javax.inject.Inject

class AddCounterFragment : Fragment() {

    companion object {
        fun newInstance() = AddCounterFragment()
    }

    private lateinit var navigator: NavigationDelegate

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: AddCounterViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getAppInjector().inject(this)
        return inflater.inflate(R.layout.fragment_add_counter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(factory)
        btn_add_counter.setOnClickListener { onClickAddCounter() }
    }

    fun onClickAddCounter() {
        val title = et_counter_name.text.toString().trim()
        val type = et_counter_type.text.toString().trim()

        if (title.isNotEmpty() || type.isNotEmpty()) {
            viewModel.addCounter(title, type, value = 0)
        }

        navigator.navigateToCounterListFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is NavigationDelegate) {
            navigator = context
        }
    }
}