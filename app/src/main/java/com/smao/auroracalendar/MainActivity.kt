package com.smao.auroracalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.*
import com.smao.auroracalendar.databinding.ActivityMainBinding
import com.smao.auroracalendar.horizontalcalendarpaging.*


class MainActivity : AppCompatActivity(), OnChangeDate {
    private lateinit var binding: ActivityMainBinding
    private var mainViewModel: MainViewModel? = null
    private lateinit var layoutManager: CalendarLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val dateSelectionAdapter = DateSelectionAdapter( { dateDetailsUI ->
            setSelectedDate(dateDetailsUI)
        },  this@MainActivity )
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
            //setItemViewCacheSize(4)
            layoutManager = this@MainActivity.layoutManager
            rvDates.smoothScrollToPosition(21)

            val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
            addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            addItemDecoration(BoundsOffsetDecoration())
        }

        layoutManager.callback = object: CalendarLayoutManager.OnItemSelectedListener {
            override fun onItemSelected(layoutPosition: Int) {
                val position = layoutManager.findLastCompletelyVisibleItemPosition()
                binding.dateTv.text = position.toString()
                Log.d("SlideSuccess", "it was swiped : $position" )
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setSelectedDate(dateDetailsUI: DateDetailsUI) {
        binding.dateTv.text = """${dateDetailsUI.day} ${dateDetailsUI.monthName}
                """.trimIndent()
    }

//    @SuppressLint("SetTextI18n")
//    override fun changeDate(day: String, month: String) {
//        binding.dateTv.text = """$day $month
//                """.trimIndent()
//    }

    @SuppressLint("SetTextI18n")
    override fun changeDate(dateDetailsUI: DateDetailsUI) {
        binding.dateTv.text = """${dateDetailsUI.day} ${dateDetailsUI.monthName}
                """.trimIndent()
    }
}