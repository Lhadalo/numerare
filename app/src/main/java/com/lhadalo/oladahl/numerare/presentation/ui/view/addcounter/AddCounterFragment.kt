package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.databinding.FragmentAddUpdateCounterBinding
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.ui.activity.NavigationDelegate
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterViewModel.Companion.ERROR
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterViewModel.Companion.SUCCESS
import com.lhadalo.oladahl.numerare.util.AlarmReceiver
import com.lhadalo.oladahl.numerare.util.extensions.bind
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.extensions.observe
import com.lhadalo.oladahl.numerare.util.helpers.DialogHelper
import com.lhadalo.oladahl.numerare.util.helpers.NotificationHelper
import com.lhadalo.oladahl.numerare.util.helpers.withViewModel
import kotlinx.android.synthetic.main.fragment_add_update_counter.*
import javax.inject.Inject

class AddCounterFragment : Fragment() {

    companion object {
        const val TAG = "AddCounterFragment"
        const val COUNTER_ITEM = "counter_item"
        fun newInstance() = AddCounterFragment()
        fun newInstance(counter: CounterItem?) = AddCounterFragment().apply {
            counter?.let { arguments = Bundle().apply { putParcelable(COUNTER_ITEM, counter) } }
        }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var navigator: NavigationDelegate
    private lateinit var viewModel: AddCounterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getAppInjector().inject(this)

        val counter = arguments?.getParcelable(COUNTER_ITEM) as CounterItem?

        val binding: FragmentAddUpdateCounterBinding =
                container?.bind(R.layout.fragment_add_update_counter) as FragmentAddUpdateCounterBinding

        viewModel = withViewModel(factory) {
            observe(state) { it?.let { renderState(it) } }
            observe(result) { it?.let { onResult(it) } }

            if (counter != null) {
                setIsEditMode()
                this.counter = counter
                binding.counter = this.counter
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachUI()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is NavigationDelegate) {
            navigator = context
        }
    }

    override fun onResume() {
        super.onResume()

        if (!viewModel.isInEditMode()) {
            val inputManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            et_counter_name.apply {
                postDelayed({
                    requestFocus()
                    inputManager.showSoftInput(et_counter_name, InputMethodManager.SHOW_IMPLICIT)
                }, 500)
            }
        }
    }

    private fun attachUI() {
        if (viewModel.isInEditMode()) {
            btn_image_delete.setOnClickListener {
                DialogHelper.confirmDelete(context, viewModel::deleteCounter)
            }
        }

        btn_add_update_counter.apply {
            if (viewModel.isInEditMode()) setOnClickListener { viewModel.updateCounter(getTitle(), getTypeDesc()) }
            else setOnClickListener { viewModel.addCounter(getTitle(), getTypeDesc()) }
        }


        btn_add_reminder.setOnClickListener {
            AddReminderDialog.show(this, viewModel::onAddReminder)
        }

        btn_image_cancel.setOnClickListener { navigator.popBackStack() }

        btn_layout_more_options.setOnClickListener { viewModel.checkLayoutMore() }

        switch_enable_auto.setOnClickListener { viewModel.checkLayoutAuto() }
        layout_switch_enable_auto.setOnClickListener { viewModel.checkLayoutAuto() }

        switch_enable_reset.setOnClickListener { viewModel.checkLayoutReset() }
        layout_switch_enable_reset.setOnClickListener { viewModel.checkLayoutReset() }
    }

    private fun renderState(state: ViewState) {
        switch_enable_reset.isChecked = state.resetSwitchChecked
        layout_reset.visibility = if (state.resetSwitchChecked) View.VISIBLE else View.GONE

        switch_enable_auto.isChecked = state.autoSwitchChecked
        layout_auto_counter.visibility = if (state.autoSwitchChecked) View.VISIBLE else View.GONE

        layout_more_options.visibility = if (state.showMoreLayoutVisible) View.VISIBLE else View.GONE

        btn_image_delete.visibility = if (state.inEditMode) View.VISIBLE else View.INVISIBLE
    }

    private fun onResult(result: Int) {
        when (result) {
            SUCCESS -> navigator.navigateToCounterListFragment()
            ERROR -> et_counter_name.error = "Error"
        }
    }


    private fun getTitle() = et_counter_name.text.toString().trim()

    private fun getTypeDesc() = et_counter_type.text.toString().trim()
}

