package com.lhadalo.oladahl.numerare.presentation.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.data.CounterEntity
import com.lhadalo.oladahl.numerare.util.extensions.inflate
import kotlinx.android.synthetic.main.list_item_counters.view.*
import kotlinx.android.synthetic.main.list_item_header.view.*

class CounterListAdapter(private val callback: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val counters = ArrayList<CounterEntity>()

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.list_item_header))
            TYPE_ITEM -> CounterViewHolder(parent.inflate(R.layout.list_item_counters))
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind()
        }
        else if (holder is CounterViewHolder) {
            holder.bind(counters[position-1], callback)
        }
    }

    override fun getItemCount() = counters.size + 1

    override fun getItemViewType(pos: Int) = if (pos == 0) TYPE_HEADER else TYPE_ITEM

    fun addCounters(counters: List<CounterEntity>?) {
        this.counters.clear()
        counters?.let { this.counters.addAll(counters) }
        notifyDataSetChanged()
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() = with(itemView) {
            tv_header_text.text = resources.getText(R.string.app_name)
        }
    }

    class CounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(counter: CounterEntity, callback: (Int) -> Unit) = with(itemView) {
            tv_counter_title.text = counter.title
            tv_counter_type.text = counter.type
            tv_counter_value.text = counter.value.toString()

            setOnClickListener { callback(counter.id) }
        }
    }
}