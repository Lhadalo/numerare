package com.lhadalo.oladahl.numerare.presentation.ui.view.counterdetail

import android.content.Context
import android.os.Bundle
import android.view.*
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

        fun newInstance(id: Int): CounterDetailFragment {
            return CounterDetailFragment().apply {
                arguments = Bundle().apply { putInt(COUNTER_ITEM, id) }
            }
        }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var navigator: NavigationDelegate
    private lateinit var viewModel: CounterDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        getAppInjector().inject(this)

        val binding = container?.bind(R.layout.fragment_counter_detail) as FragmentCounterDetailBinding

        viewModel = withViewModel(factory) {
            counterId = arguments?.getInt(COUNTER_ITEM)

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

        btn_image_cancel.setOnClickListener { navigator.popBackStack() }
        btn_image_edit.setOnClickListener { viewModel.counter.value?.let { navigator.navigateToAddCounterFragment(it) } }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is NavigationDelegate) {
            navigator = context
        }
    }
}