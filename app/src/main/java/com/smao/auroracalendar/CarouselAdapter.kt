package com.smao.auroracalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.smao.auroracalendar.databinding.ItemListBinding

class CarouselAdapter(private val cards: List<Days>) :
    RecyclerView.Adapter<CarouselAdapter.MyViewHolder>()
{

    lateinit var binding: ItemListBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemListBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        binding.dayTv.text = cards[position].day
        binding.monthTv.text = cards[position].month


        myViewHolder.binding.oneItem.setOnClickListener {
            val rv = myViewHolder.binding.root.parent as RecyclerView?
            if (rv != null) {
                rv.smoothScrollToCenteredPosition(position)
            }
        }
    }

    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}

private fun RecyclerView.smoothScrollToCenteredPosition(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
            val dxToStart = super.calculateDxToMakeVisible(view, SNAP_TO_START)
            val dxToEnd = super.calculateDxToMakeVisible(view, SNAP_TO_END)

            return (dxToStart + dxToEnd) / 2
        }
    }

    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}
