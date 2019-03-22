package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.databinding.FragmentAddUpdateCounterBinding
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.presentation.ui.activity.NavigationDelegate
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterViewModel.Companion.ERROR
import com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter.AddCounterViewModel.Companion.SUCCESS
import com.lhadalo.oladahl.numerare.util.extensions.bind
import com.lhadalo.oladahl.numerare.util.extensions.getAppInjector
import com.lhadalo.oladahl.numerare.util.extensions.observe
import com.lhadalo.oladahl.numerare.util.helpers.DialogHelper
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

    var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun tintMenuIcon(item: MenuItem, @ColorRes color: Int) {
        val wrapDrawable = DrawableCompat.wrap(item.icon)
        DrawableCompat.setTint(wrapDrawable, ResourcesCompat.getColor(resources, color, null))
        item.icon = wrapDrawable
    }

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
                mapToViewState(counter)
                binding.counter = this.counter
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = (activity as AppCompatActivity)
        activity.setSupportActionBar(add_update_toolbar)


        val upArrow: Drawable = resources.getDrawable(R.drawable.ic_clear_white_24dp, null)
        activity.supportActionBar?.setHomeAsUpIndicator(upArrow)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val collapsingToolbarLayout = collapsing_toolbar_layout
        collapsingToolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.black))
        collapsingToolbarLayout.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

        val statusBarHeight = statusBarHeight()

        menuItem?.let {
            tintMenuIcon(it, R.color.black)

        }
        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbar, verticalOffset ->
            if ((collapsing_toolbar_layout.scrimVisibleHeightTrigger + statusBarHeight) - Math.abs(verticalOffset) < 0) {
                //  Collapsed
                upArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            } else {
                //Expanded
                upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        })

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
            val inputManager: InputMethodManager =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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
            btn_delete_counter.visibility = View.VISIBLE
            btn_delete_counter.setOnClickListener {
                DialogHelper.confirmDelete(context, viewModel::deleteCounter)
            }
        }

        btn_add_update_counter.apply {
            if (viewModel.isInEditMode())
                setOnClickListener { viewModel.updateCounter(getTitle()) }
            else
                setOnClickListener { viewModel.addCounter(getTitle()) }
        }

        btn_add_reminder.setOnClickListener {
            if (viewModel.hasReminder()) viewModel.clearReminder()
            else AddReminderDialog.show(this, viewModel::onAddReminder)
        }

        //btn_image_cancel.setOnClickListener { navigator.popBackStack() }

        btn_layout_more_options.setOnClickListener { viewModel.checkLayoutMore() }

        switch_enable_auto.setOnClickListener { viewModel.checkLayoutAuto() }
        layout_switch_enable_auto.setOnClickListener { viewModel.checkLayoutAuto() }

        switch_enable_reset.setOnClickListener { viewModel.checkLayoutReset() }
        layout_switch_enable_reset.setOnClickListener { viewModel.checkLayoutReset() }
    }

    private fun renderState(state: ViewState) {
        //Reset state
        switch_enable_reset.isChecked = state.resetSwitchChecked
        layout_reset.visibility = if (state.resetSwitchChecked) View.VISIBLE else View.GONE

        //Auto Counter state
        switch_enable_auto.isChecked = state.autoSwitchChecked
        layout_auto_counter.visibility = if (state.autoSwitchChecked) View.VISIBLE else View.GONE

        //Delete button state
        //btn_image_delete.visibility = if (state.inEditMode) View.VISIBLE else View.INVISIBLE

        //Reminder State
        tv_reminder.text = state.reminderText
        if (state.hasReminder) {
            tv_reminder.setTextColor(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
            btn_add_reminder.text = resources.getText(R.string.button_reminder_text_clear)
        } else {
            tv_reminder.setTextColor(layout_more_less_title.textColors)
            btn_add_reminder.text = resources.getText(R.string.button_reminder_text_add)
        }

        //More visible state
        layout_more_options.visibility = if (state.showMoreLayoutVisible) View.VISIBLE else View.GONE
        if (state.showMoreLayoutVisible) {
            layout_more_less_title.text = resources.getText(R.string.tv_less)
            iv_btn_layout_more.setImageDrawable(context?.getDrawable(R.drawable.ic_outline_keyboard_arrow_up_24px))
        } else {
            layout_more_less_title.text = resources.getText(R.string.tv_more)
            iv_btn_layout_more.setImageDrawable(context?.getDrawable(R.drawable.ic_outline_keyboard_arrow_down_24px))
        }

        //Button Add Update state
        btn_add_update_counter.text =
                if (state.inEditMode) resources.getText(R.string.title_button_update)
                else resources.getText(R.string.title_button_add)

    }

    private fun onResult(result: Int) {
        when (result) {
            SUCCESS -> navigator.navigateToCounterListFragment()
            ERROR -> et_counter_name.error = "Error"
        }
    }

    private fun statusBarHeight(): Int {
        return (24 * resources.displayMetrics.density).toInt()
    }

    private fun getTitle() = et_counter_name.text.toString().trim()

}

