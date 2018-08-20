package com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.databinding.FragmentCounterDetailBinding
import com.lhadalo.oladahl.numerare.presentation.ui.activity.NavigationDelegate
import com.lhadalo.oladahl.numerare.util.extensions.bind
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.helpers.withViewModel
import kotlinx.android.synthetic.main.fragment_counter_detail.*
import javax.inject.Inject

class CounterDetailFragment : Fragment() {

    companion object {
        private const val COUNTER_ITEM = "counter_item"

        fun newInstance(counterId: Long): CounterDetailFragment {
            return CounterDetailFragment().apply {
                arguments = Bundle().apply { putLong(COUNTER_ITEM, counterId) }
            }
        }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var navigator: NavigationDelegate
    private lateinit var viewModel: CounterDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        getAppInjector().inject(this)

        val binding = container?.bind(R.layout.fragment_counter_detail) as FragmentCounterDetailBinding

        viewModel = withViewModel(factory) {
            counterId = arguments?.getLong(COUNTER_ITEM)

            binding.let {
                it.viewmodel = this
                it.setLifecycleOwner(this@CounterDetailFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_plus.setOnClickListener { viewModel.onClickPlus() }
        btn_minus.setOnClickListener { viewModel.onClickMinus() }

        val appCompatActivity = activity as AppCompatActivity
        val toolbar = appCompatActivity.setSupportActionBar(detail_toolbar)
        appCompatActivity.supportActionBar?.title = ""

        btn_show_history.setOnClickListener { _ ->
            viewModel.counter.value?.let {
                navigator.navigateToHistory(it.id, it.counterValue)
            }
        }
//        btn_image_cancel.setOnClickListener { navigator.popBackStack() }
//        btn_image_edit.setOnClickListener { viewModel.counter.value?.let { navigator.navigateToAddCounterFragment(it) } }
//
//        btn_image_restore.setOnClickListener { viewModel.restoreValue() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_counter_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> navigator.popBackStack()
            R.id.action_edit -> viewModel.counter.value?.let { navigator.navigateToAddCounterFragment(it) }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is NavigationDelegate) {
            navigator = context
        }
    }
}