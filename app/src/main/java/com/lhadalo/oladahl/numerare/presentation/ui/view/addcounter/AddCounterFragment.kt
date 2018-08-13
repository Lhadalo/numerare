package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.ui.activity.NavigationDelegate
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.helpers.withViewModel
import kotlinx.android.synthetic.main.fragment_add_counter.*
import javax.inject.Inject

class AddCounterFragment : Fragment() {

    companion object {
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
    private lateinit var addReminderDialog: AddReminderDialog

    private var moreShown = false
    private var resetSwitchChecked = false
    private var autoSwitchChecked = false
    private var editMode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getAppInjector().inject(this)

        val counter = arguments?.getParcelable(COUNTER_ITEM) as CounterItem?
        editMode = counter != null

        viewModel = withViewModel(factory) {
            if (counter != null) counterItem = counter
        }
        return inflater.inflate(R.layout.fragment_add_counter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addReminderDialog = AddReminderDialog(context, viewModel::onAddReminder)

        attachUI()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is NavigationDelegate) {
            navigator = context
        }
    }

    private fun attachUI() {
        btn_add_counter.setOnClickListener { onClickAddCounter() }
        btn_add_reminder.setOnClickListener { addReminderDialog.show() }
        btn_image_cancel.setOnClickListener { navigator.popBackStack() }

        btn_layout_more_options.setOnClickListener { onClickShowMore() }
        switch_enable_auto.setOnCheckedChangeListener { _, isChecked -> onCheckAutoSwitch(isChecked) }
        layout_switch_enable_auto.setOnClickListener { switch_enable_auto.isChecked = !autoSwitchChecked }

        switch_enable_reset.setOnCheckedChangeListener { _, isChecked -> onCheckResetSwitch(isChecked)}
        layout_switch_enable_reset.setOnClickListener { switch_enable_reset.isChecked = !resetSwitchChecked }

        if (editMode) {
            et_counter_name.setText(viewModel.counterItem.title)
            et_counter_type.setText(viewModel.counterItem.typeDesc)

            btn_image_delete.visibility = View.VISIBLE
            btn_image_delete.setOnClickListener { onClickDelete() }
        }
    }

    private fun onCheckResetSwitch(isChecked: Boolean) {
        layout_reset.visibility = if (isChecked) View.VISIBLE else View.GONE
        resetSwitchChecked = isChecked
    }

    private fun onCheckAutoSwitch(isChecked: Boolean) {
        layout_auto_counter.visibility = if (isChecked) View.VISIBLE else View.GONE
        autoSwitchChecked = isChecked
    }

    private fun onClickShowMore() {
        layout_more_options.visibility = if (moreShown) View.GONE else View.VISIBLE
        moreShown = !moreShown
    }

    private fun onClickDelete() {
        ConfirmDialog(context).confirmDelete(viewModel::deleteCounter)
    }

    private fun onClickAddCounter() {
        val title = et_counter_name.text.toString().trim()
        val type = et_counter_type.text.toString().trim()

        if (title.isNotEmpty()) {
            if (type.isNotEmpty()) viewModel.addCounter(title, type)
            else viewModel.addCounter(title, "Count")
        }

        navigator.popBackStack()
    }

}

