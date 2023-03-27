package com.smao.auroracalendar.horizontalcalendarpaging

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.smao.auroracalendar.databinding.ItemListBinding

class DateSelectionAdapter(private val onDateSelection: (DateDetailsUI) -> Unit) :
    PagingDataAdapter<DateDetailsUI, DateSelectionAdapter.DateViewHolder>(dateDifferentiators) {
    private val newWidth = Resources.getSystem().displayMetrics.widthPixels / 3

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = ItemListBinding.inflate(LayoutInflater.from(parent.context))
        return DateViewHolder(view)
    }


    inner class DateViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dateDetailsUI: DateDetailsUI) {
            binding.dayTv.text = dateDetailsUI.day.toString()
            binding.monthTv.text = dateDetailsUI.monthName
            itemView.setOnClickListener {
                onDateSelection.invoke(dateDetailsUI)
                val rv = binding.root.parent as RecyclerView?
                if (rv != null) {
                    rv.smoothScrollToCenteredPosition(position)
                }
            }
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            params.width = newWidth
            itemView.layoutParams = params
        }
    }
}

val dateDifferentiators = object : DiffUtil.ItemCallback<DateDetailsUI>() {
    override fun areItemsTheSame(oldItem: DateDetailsUI, newItem: DateDetailsUI): Boolean {
        return oldItem.dateKey == newItem.dateKey
    }

    override fun areContentsTheSame(oldItem: DateDetailsUI, newItem: DateDetailsUI): Boolean {
        return oldItem == newItem
    }
}

private fun RecyclerView.smoothScrollToCenteredPosition(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
            val dxToStart = super.calculateDxToMakeVisible(view, SNAP_TO_START)
            val dxToEnd = super.calculateDxToMakeVisible(view, SNAP_TO_END)
            return (dxToStart + dxToEnd) / 2
            Log.d("PROVERKA", "DAAAAA 222")
        }

    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)


}
