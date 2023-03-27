package com.smao.auroracalendar.horizontalcalendarpaging

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.roundToInt

class CalendarLayoutManager(
    context: Context,
    private val minScaleDistanceFactor: Float = 1.5f,
    private val scaleDownBy: Float = 0.10f
) : LinearLayoutManager(context, HORIZONTAL, false) {

    var callback:OnItemSelectedListener? = null
    private lateinit var recyclerView: RecyclerView

    override fun onLayoutCompleted(state: RecyclerView.State?) =
        super.onLayoutCompleted(state).also { scaleChildren() }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view!!
        // Smart snapping
        LinearSnapHelper().attachToRecyclerView(recyclerView)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        scaleChildren()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) = super.scrollHorizontallyBy(dx, recycler, state).also {
        if (orientation == HORIZONTAL) scaleChildren()
    }

    private fun scaleChildren() {
        val mid = width / 2f

        // Any view further than this threshold will be fully scaled down
        val scaleDistanceThreshold = 0.9f * mid

        var translationXForward = 0f

        for (i in 0 until childCount) {
            val child = getChildAt(i)!!
            val childMid = (child.left + child.right) / 2f
            val distanceToCenter = abs(mid - childMid)
            val scaleDownAmount = (distanceToCenter / scaleDistanceThreshold).coerceAtMost(1f)
            val scale = 1f - scaleDownBy * scaleDownAmount
            child.scaleX = scale
            child.scaleY = scale

        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        // When scroll stops we notify on the selected item
        if (state.equals(RecyclerView.SCROLL_STATE_IDLE)) {
            // Find the closest child to the recyclerView center --> this is the selected item.
            val recyclerViewCenterX = getRecyclerViewCenterX()
            var minDistance = recyclerView.width
            //var position = 0
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                val childCenterX = getDecoratedLeft(child) + (getDecoratedRight(child) - getDecoratedLeft(child)) / 2
                var newDistance = Math.abs(childCenterX - recyclerViewCenterX)
                if (newDistance < minDistance) {
                    minDistance = newDistance
                    //position = recyclerView.getChildLayoutPosition(child)
                }
            }
            // Notify on item selection
            //callback?.onItemSelected(position)
        }
        Log.d("PROVERKA", "Scrolled")
    }

    private fun getRecyclerViewCenterX() : Int {
        //return (recyclerView.right - recyclerView.left)/2 + recyclerView.left
        return (recyclerView.right - recyclerView.left)/2
    }

    interface OnItemSelectedListener {
        fun onItemSelected(layoutPosition: Int)
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
        return (width / (1 - scaleDownBy)).roundToInt()
    }
}
