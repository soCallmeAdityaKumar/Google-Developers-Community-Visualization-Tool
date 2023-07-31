package com.example.googledeveloperscommunityvisualisationtool.fragments.Calendar

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.GridView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.events.calendar.utils.EventsCalendarUtil.today
import com.events.calendar.views.EventsCalendar
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcoEventViewMod
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcomEventRepo
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcomingEventfactory
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentCalendarBinding
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.UpcomingEvents
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventRoomFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text
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
     lateinit var eventsDatabaseViewModel:UpcoEventroomViewmodel
     var date=Date()
    lateinit var listOfCalendar:Array<Calendar>
    lateinit var sharedPref: SharedPreferences
    lateinit var editor:SharedPreferences.Editor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CoroutineScope(Dispatchers.Main).launch {

        }
        binding=FragmentCalendarBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        eventsCalendar=binding.eventsCalendar

         sharedPref=activity?.getSharedPreferences("EventCalendar",Context.MODE_PRIVATE)!!
         editor=sharedPref?.edit()!!
        editor?.apply()

        listOfCalendar= arrayOf()

            val start=Calendar.getInstance()
            val end=Calendar.getInstance()
            end.add(Calendar.YEAR,2)
            eventsCalendar.setSelectionMode(eventsCalendar.SINGLE_SELECTION) //set mode of Calendar
                .setToday(today)
                .setMonthRange(start, end)
                .setWeekStartDay(Calendar.SUNDAY, false)
                .setCurrentSelectedDate(today)
                .setDateTextFontSize(16f)
                .setMonthTitleFontSize(26f)
                .setWeekHeaderFontSize(26f)
                .setCallback(this)
         .build()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)


        eventsDatabaseViewModel=ViewModelProvider(this, UpcoEventRoomFactory(requireContext())).get(UpcoEventroomViewmodel::class.java)
        eventsDatabaseViewModel.readAllEventViewModel.observe(viewLifecycleOwner,Observer{it->
            for(i in it){
                val calendar=Calendar.getInstance()
                val formatter=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ")
                calendar.setTime(formatter.parse(i.start_date))
                Log.d("Calendar","event.startdate->${i.start_date}+Calendar.time->${calendar.time}+Fulldate->${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}")
                calendar.add(Calendar.DATE,0)
                eventsCalendar.addEvent(calendar)
                editor.apply {
                    editor.putString("${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}",i.title)
                    apply()
                }
            }
        })
        eventsCalendar.setCallback(this)

    }
    override fun onResume() {
        super.onResume()
        val customAppBar = (activity as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton
        val backButton = customAppBar.backarrow

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton?.visibility = View.VISIBLE
            backButton?.visibility = View.GONE
        } else {
            menuButton?.visibility = View.GONE
            backButton?.visibility = View.VISIBLE
        }

        backButton?.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

    }

    override fun onDayLongPressed(selectedDate: Calendar?) {
        Log.e("LONG", "CLICKED")
    }

    override fun onDaySelected(selectedDate: Calendar?) {
        Log.e("SHORT",selectedDate?.time.toString())
//        ${getDateString(selectedDate?.timeInMillis)}
        val dateString="${selectedDate?.get(Calendar.DATE)}-${selectedDate?.get(Calendar.MONTH)}-${selectedDate?.get(Calendar.YEAR)}"
        selectedDate!!.time
        if(eventsCalendar.hasEvent(selectedDate)){
            showEventDetail(dateString)
        }

    }

    private fun showEventDetail(date:String) {
        val dialog= Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)

        val eventDate:TextView=dialog.findViewById(R.id.bottom_sheet_date)
        val evenTitle:TextView=dialog.findViewById(R.id.event_bottom_Title)

        eventDate.text=date
        evenTitle.text=sharedPref.getString(date,"")

        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations=R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
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