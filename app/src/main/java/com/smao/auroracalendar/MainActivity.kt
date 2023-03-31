package com.smao.auroracalendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.smao.auroracalendar.databinding.ActivityMainBinding
import com.smao.auroracalendar.horizontalcalendarpaging.*
import kotlin.math.abs
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mainViewModel: MainViewModel? = null
    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val dateSelectionAdapter = DateSelectionAdapter { dateDetailsUI ->
            setSelectedDate(dateDetailsUI)
        }
        val rvDates = binding.recyclerView
        rvDates.adapter = dateSelectionAdapter

        mainViewModel!!.dateDetailsList.observe(this) { integerPagingData ->
            dateSelectionAdapter.submitData(lifecycle, integerPagingData)
        }
        mainViewModel!!.selectedDate.observe(this) { selectedDate ->
            val dateDetailsUI = selectedDate.toDate()?.toDateDetails()
            dateDetailsUI?.let {
                setSelectedDate(it)
            }
        }
        mainViewModel!!.resetDateList.observe(this) { timeInMillis ->
            if (timeInMillis != null) {
                dateSelectionAdapter.submitData(lifecycle, PagingData.empty())

            }
        }
        layoutManager = CalendarLayoutManager(this)
        //snapHelper = PagerSnapHelper()
        with(rvDates) {
            setItemViewCacheSize(4)
            layoutManager = this@MainActivity.layoutManager
            rvDates.smoothScrollToPosition(21)

            val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
            addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            addItemDecoration(BoundsOffsetDecoration())
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setSelectedDate(dateDetailsUI: DateDetailsUI) {
        binding.dateTv.text = """${dateDetailsUI.day} ${dateDetailsUI.monthName}
                """.trimIndent()
    }
}