package com.smao.auroracalendar

import com.smao.auroracalendar.horizontalcalendarpaging.DateDetailsUI

interface OnChangeDate {
    fun changeDate(dateDetailsUI: DateDetailsUI)
}

//interface OnChangeDate {
//    fun changeDate(day: String, month: String)
//}