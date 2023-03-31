package com.smao.auroracalendar.horizontalcalendarpaging

import androidx.lifecycle.*
import androidx.paging.*
import java.util.*

class MainViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData(Date().toStringFormat())

    val selectedDate: LiveData<String>
        get() = _selectedDate

    private val dateSelectionPager: LiveData<PagingData<Date>> =
        Transformations.switchMap(selectedDate) { changedSelectedDate ->
            return@switchMap Pager(PagingConfig(pageSize = 10)) {
                DateSelectionPageSource(changedSelectedDate)
            }.liveData.cachedIn(viewModelScope)
        }

    val dateDetailsList = dateSelectionPager.map { pagingData ->
        pagingData.map { mealDate ->
            mealDate.toDateDetails()
        }
    }

    private val _resetDateList = MutableLiveData<Long>()

    val resetDateList: LiveData<Long>
        get() = _resetDateList
}