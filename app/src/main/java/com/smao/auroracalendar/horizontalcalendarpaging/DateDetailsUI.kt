package com.smao.auroracalendar.horizontalcalendarpaging

import java.util.*

data class DateDetailsUI(
    val dateKey: String,
    val day: Int,
    //val year: Int,
    val monthName: String,
    //val dayOfWeek: String,
    val date: Date,
)

fun Date.toDateDetails(): DateDetailsUI {
    val calendar = GregorianCalendar()
    calendar.time = this

    return DateDetailsUI(
        dateKey = calendar.time.toStringFormat(),
        day = calendar.get(Calendar.DAY_OF_MONTH),
        //year = calendar.get(Calendar.YEAR),
        monthName = monthOfYear[calendar.get(Calendar.MONTH)],
        //dayOfWeek = dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1],
        date = calendar.time,
    )
}

val monthOfYear = listOf(
    "JANUARY",
    "FEBRUARY",
    "MARCH",
    "APRIL",
    "MAY",
    "JUNE",
    "JULY",
    "AUGUST",
    "SEPTEMBER",
    "OCTOBER",
    "NOVEMBER",
    "DECEMBER",
)