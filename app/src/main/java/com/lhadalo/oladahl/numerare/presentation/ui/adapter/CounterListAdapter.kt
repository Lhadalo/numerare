package com.lhadalo.oladahl.numerare.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.databinding.ListItemCountersBinding
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.util.extensions.bind
import com.lhadalo.oladahl.numerare.util.extensions.inflate
import kotlinx.android.synthetic.main.list_item_header.view.*

class CounterListAdapter(private val callback: (Long) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val counters = ArrayList<CounterItem>()

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.list_item_header))
            TYPE_ITEM -> CounterViewHolder(parent.bind(R.layout.list_item_counters) as ListItemCountersBinding)
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind()
        }
        else if (holder is CounterViewHolder) {
            holder.binding.counter = counters[position-1]
            holder.binding.root.setOnClickListener { callback(counters[position-1].id) }
        }
    }

    fun getIdAndValue(position: Int): Pair<Long, Int> {
        return counters[position-1].let { it.id to it.counterValue }
    }

    override fun getItemCount() = counters.size + 1

    override fun getItemViewType(pos: Int) = if (pos == 0) TYPE_HEADER else TYPE_ITEM

    fun addCounters(counters: List<CounterItem>?) {
        this.counters.clear()
        counters?.let { this.counters.addAll(counters) }
        notifyDataSetChanged()
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() = with(itemView) {
            tv_header_text.text = resources.getText(R.string.app_name)
        }
    }



    class CounterViewHolder(val binding: ListItemCountersBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}