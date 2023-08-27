package com.aditya.googledeveloperscommunityvisualisationtool.fragments.Calendar

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.fonts.Font
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
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventRoomFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.events.calendar.utils.EventsCalendarUtil.today
import com.events.calendar.views.EventsCalendar
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentCalendarBinding
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread
import org.w3c.dom.Text
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date

class CalendarFragment : Fragment() ,EventsCalendar.Callback{

     lateinit var binding:FragmentCalendarBinding
     lateinit var viewModel: CalendarViewModel
     lateinit var eventsCalendar: EventsCalendar
     lateinit var eventsDatabaseViewModel: UpcoEventroomViewmodel
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
        val event:MutableList<String> = mutableListOf()
        if(sharedPref.all.size!=0){
            sharedPref.all.forEach{
                event.add(sharedPref.getString(it.key,"")!!)
            }
        }
        val calendar=Calendar.getInstance()
//                val formatter=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ")
//                calendar.setTime(formatter.parse(i.start_date))
        var originalFormatter = ZonedDateTime.parse("2023-09-16T19:00:00+05:30")
        val instant = originalFormatter.toInstant()
        val date = Date.from(instant)
        calendar.setTime(date)
        listOfCalendar= arrayOf()
            val start=Calendar.getInstance()
            val end=Calendar.getInstance()
            end.add(Calendar.YEAR,2)
        calendar.add(Calendar.DAY_OF_MONTH,0)
        eventsCalendar.addEvent(calendar)
        Log.d("CalendarFragment","Begin Calendar")
            eventsCalendar.setSelectionMode(eventsCalendar.SINGLE_SELECTION) //set mode of Calendar
                .setToday(today)
                .setMonthRange(start, end)
                .setWeekStartDay(Calendar.SUNDAY, false)
                .setCurrentSelectedDate(today)
                .setDateTextFontSize(26f)
                .setMonthTitleFontSize(30f)
                .setWeekHeaderFontSize(20f)
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
//                val formatter=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ")
//                calendar.setTime(formatter.parse(i.start_date))
                var originalFormatter = ZonedDateTime.parse(i.start_date)
                val instant = originalFormatter.toInstant()
                val date = Date.from(instant)
                calendar.setTime(date)
                Log.d("CalendarFragment","event.startdate->${i.start_date}+Calendar.time->${calendar.time}+Fulldate->${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]+1}-${calendar[Calendar.YEAR]}")
                calendar.add(Calendar.DAY_OF_MONTH,0)
                eventsCalendar.addEvent(calendar)
                val event=CalendarDataDetails("${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}",date,i.title,i.picture.thumbnail_url.toString())
                var eventString= Gson().toJson(event)
                if(sharedPref.contains("${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}")){
                    var eventFromPref=sharedPref.getString("${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}","")
//                    val eventlistType=object : TypeToken<MutableList<CalendarDataDetails>>() {}.type
//                    val eventClass:MutableList<CalendarDataDetails> =Gson().fromJson<CalendarDataDetails>(eventFromPref,eventlistType)
//                    eventClass.add(event)
//                    eventClass.toList()
//                    eventString=Gson().toJson(eventClass)

                    Log.d("CalendarFragment","${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]} found again with ${i.title} previous->$eventFromPref ")

                }
                editor.apply {
                    Log.d("CalendarFragment","$eventString added ")
                    editor.putString("${calendar[Calendar.DATE]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}",eventString)
                    apply()
                }
            }
        })
        eventsCalendar.setCallback(this)


    }
    override fun onResume() {
        super.onResume()
        loadConnectionStatus()
        val customAppBar = (activity as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton.setBackgroundResource(R.drawable.baseline_menu_24)
//            menuButton?.visibility = View.VISIBLE
//            backButton?.visibility = View.GONE
        } else {
            menuButton.setBackgroundResource(R.drawable.backarrow)
//            menuButton?.visibility = View.GONE
//            backButton?.visibility = View.VISIBLE
            menuButton?.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }
        }

    }
    private fun loadConnectionStatus() {
        val sharedPreferences = activity?.getSharedPreferences(
            ConstantPrefs.SHARED_PREFS.name,
            Context.MODE_PRIVATE
        )

        val isConnected = sharedPreferences?.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        val act=activity as MainActivity
        if (isConnected!!) {
            act.binding.appBarMain.LGConnected.visibility=View.VISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.INVISIBLE
        } else {
            act.binding.appBarMain.LGConnected.visibility=View.INVISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.VISIBLE
        }
    }

    override fun onDayLongPressed(selectedDate: Calendar?) {
        Log.e("CalendarFragment", "LONG->CLICKED")
    }

    override fun onDaySelected(selectedDate: Calendar?) {
        Log.e("CalendarFragment","SHORT->${selectedDate?.time.toString()}")
//        ${getDateString(selectedDate?.timeInMillis)}
        val dateString="${selectedDate?.get(Calendar.DATE)}-${selectedDate?.get(Calendar.MONTH)}-${selectedDate?.get(Calendar.YEAR)}"
        selectedDate!!.time
        val calendar=Calendar.getInstance()
//                val formatter=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ")
//                calendar.setTime(formatter.parse(i.start_date))
        var originalFormatter = selectedDate.toInstant()
        val date = Date.from(originalFormatter)
        calendar.setTime(date)

        Log.d("CalendarFragment","selected Date->$date,Date->${calendar.get(Calendar.DATE)},Month->${calendar.get(Calendar.MONTH)},Year->${calendar.get(Calendar.YEAR)}")
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
        val logo:CircleImageView=dialog.findViewById(R.id.eventLogo)

        Log.d("CalendarFragment","Data selected for event ->$date")
        val eventString=sharedPref.getString(date,"")
        val eventlistType=object : TypeToken<CalendarDataDetails>() {}.type
        val eventClass:CalendarDataDetails =Gson().fromJson(eventString,eventlistType)

        evenTitle.text=eventClass.title
        eventDate.text=eventClass.actualDate.toString().dropLast(18)
        if(eventClass.logoLink.isNotEmpty()){
            Glide.with(binding.root)
                .load(eventClass.logoLink)
                .centerCrop()
                .into(logo)
        }else{
            Glide.with(binding.root)
                .load(resources.getDrawable(R.drawable.gdglogo))
                .centerCrop()
                .into(logo)
        }
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