package com.lhadalo.oladahl.numerare.presentation.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lhadalo.oladahl.numerare.R
import com.lhadalo.oladahl.numerare.presentation.model.CounterItem
import com.lhadalo.oladahl.numerare.util.extensions.inflate
import com.lhadalo.oladahl.numerare.util.helpers.AutoUpdatableAdapter
import kotlinx.android.synthetic.main.list_item_counters.view.*
import kotlin.properties.Delegates

class CounterListAdapter(private val callback: (Long) -> Unit) : RecyclerView.Adapter<CounterListAdapter.CounterViewHolder>(), AutoUpdatableAdapter {
    var items: List<CounterItem> by Delegates.observable(emptyList()) { prop, old, new ->
        autoNotify(old, new) { o, n -> o.id == n.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder =
            CounterViewHolder(parent.inflate(R.layout.list_item_counters))

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { callback(items[position].id) }
    }

    fun getId(position: Int): Long = items[position].id

    override fun getItemCount() = items.size

    class CounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       fun bind(c: CounterItem) = with(itemView) {
           tv_counter_title.text = c.title
           tv_counter_value.text = c.counterValue.toString()
       }
    }
}