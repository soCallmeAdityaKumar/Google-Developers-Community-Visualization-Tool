package com.example.googledeveloperscommunityvisualisationtool.fragments.Calendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.events.calendar.utils.EventsCalendarUtil.today
import com.events.calendar.views.EventsCalendar
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class CalendarFragment : Fragment() ,EventsCalendar.Callback{

     lateinit var binding:FragmentCalendarBinding
     lateinit var viewModel: CalendarViewModel
     lateinit var eventsCalendar: EventsCalendar
     var date=Date()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCalendarBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        eventsCalendar=binding.eventsCalendar

        val start=Calendar.getInstance()
        val end=Calendar.getInstance()
        end.add(Calendar.YEAR,2)
        eventsCalendar.setSelectionMode(eventsCalendar.SINGLE_SELECTION) //set mode of Calendar
            .setToday(today) //set today's date [today: Calendar]
            .setMonthRange(start, end) //set starting month [start: Calendar] and ending month [end: Calendar]
            .setWeekStartDay(Calendar.SUNDAY, false) //set start day of the week as you wish [startday: Int, doReset: Boolean]
            .setCurrentSelectedDate(today) //set current date and scrolls the calendar to the corresponding month of the selected date [today: Calendar]
//            .setDatesTypeface(typeface) //set font for dates
            .setDateTextFontSize(16f) //set font size for dates
//            .setMonthTitleTypeface(typeface) //set font for title of the calendar
            .setMonthTitleFontSize(26f) //set font size for title of the calendar
//            .setWeekHeaderTypeface(typeface) //set font for week names
            .setWeekHeaderFontSize(16f) //set font size for week names
            .setCallback(this) //set the callback for EventsCalendar
//            .addEvent(c) //set events on the EventsCalendar [c: Calendar]
//            .disableDate(dc) //disable a specific day on the EventsCalendar [c: Calendar]
//            .disableDaysInWeek(Calendar.SATURDAY, Calendar.SUNDAY) //disable days in a week on the whole EventsCalendar [varargs days: Int]
            .build()

        val c=Calendar.getInstance()
        val formatter= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ")
        val date=c.setTime(formatter.parse("2023-07-31T12:00:00+06:00"))
        c.add(Calendar.DATE,0)
        eventsCalendar.addEvent(c)
        Log.d("events",eventsCalendar.hasEvent(c).toString())
//        eventsCalendar.clearEvents()


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDayLongPressed(selectedDate: Calendar?) {
        Log.e("LONG", "CLICKED")
    }

    override fun onDaySelected(selectedDate: Calendar?) {
        Log.e("SHORT", getDateString( selectedDate?.timeInMillis))
//        ${getDateString(selectedDate?.timeInMillis)}

    }

    override fun onMonthChanged(monthStartDate: Calendar?) {
        Log.e("MON", "CHANGED")
    }
    private fun getDateString(time: Long?): String {
        if (time != null) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = time
            val month = when (cal[Calendar.MONTH]) {
                Calendar.JANUARY -> "January"
                Calendar.FEBRUARY -> "February"
                Calendar.MARCH -> "March"
                Calendar.APRIL -> "April"
                Calendar.MAY -> "May"
                Calendar.JUNE -> "June"
                Calendar.JULY -> "July"
                Calendar.AUGUST -> "August"
                Calendar.SEPTEMBER -> "September"
                Calendar.OCTOBER -> "October"
                Calendar.NOVEMBER -> "November"
                Calendar.DECEMBER -> "December"
                else -> ""
            }
            return "$month ${cal[Calendar.DAY_OF_MONTH]}, ${cal[Calendar.YEAR]}"
        } else return ""
    }



}