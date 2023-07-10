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
import java.util.Calendar
import java.util.Date

class CalendarFragment : Fragment() ,EventsCalendar.Callback,EventInterface{

    private lateinit var binding:FragmentCalendarBinding
    private lateinit var viewModel: CalendarViewModel
    private lateinit var eventsCalendar: EventsCalendar

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
        Log.e("SHORT", "CLICKED")

    }

    override fun onMonthChanged(monthStartDate: Calendar?) {
        Log.e("MON", "CHANGED")
    }

    override fun addUpcomingEvents(date: String) {
        eventsCalendar.addEvent(date)
    }


}