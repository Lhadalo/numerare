package com.lhadalo.oladahl.numerare.presentation.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.ResetItem
import com.lhadalo.oladahl.numerare.util.extensions.inflate
import kotlinx.android.synthetic.main.list_item_history.view.*
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


class HistoryListAdapter : RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {
    private val historyList = ArrayList<ResetItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_history))
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    fun addItems(list: List<ResetItem>?) {
        historyList.clear()
        list?.let {  historyList.addAll(it) }
        notifyDataSetChanged()
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(resetItem: ResetItem) = with(itemView) {
            tv_history_date.text = formatDateInterval(resetItem.initDate, resetItem.restoreDate)
            tv_history_value.text = resetItem.counterValue.toString()
        }


        private fun formatDateInterval(initDate: OffsetDateTime?, restoreDate: OffsetDateTime?): String {
            initDate?.let { init ->
                restoreDate?.let { restore ->
                    val f: DateTimeFormatter = if (sameDate(init, restore)) {
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    } else {
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    }
                    return String.format("%s–%s", init.format(f), restore.format(f))
                }
            }

            return "error"
        }

        private fun sameDate(iDate: OffsetDateTime, rDate: OffsetDateTime): Boolean {
            return (iDate.year == rDate.year) &&
                    (iDate.month == rDate.month) &&
                    (iDate.dayOfMonth == rDate.dayOfMonth)
        }
    }
}