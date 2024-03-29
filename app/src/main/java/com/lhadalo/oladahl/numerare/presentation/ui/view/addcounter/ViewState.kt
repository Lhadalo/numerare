package com.lhadalo.oladahl.numerare.presentation.ui.view.addcounter

data class ViewState(
        val showMoreLayoutVisible: Boolean = false,
        val autoSwitchChecked: Boolean = false,
        val resetSwitchChecked: Boolean = false,
        val reminderText: String = "Reminder",
        val hasReminder: Boolean = false,
        val inEditMode: Boolean = false,
        val hasError: Boolean = false
)