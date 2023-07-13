package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.app.AlarmManager
import android.app.Notification
import android.app.Notification.BigTextStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.R.drawable
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldGdgOrganAdap
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventDetailsBinding
import com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.OrganizersAdapter
import com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.events
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.AlarmReceiver
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.UpcomingEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

class UpcomingEventDetails : Fragment() {
    val url:UpcomingEventDetailsArgs by navArgs()
    private  val notification=101
    private  val CHANNEL_ID="Upcoming_Event_Notification"
    private lateinit var calendar: Calendar
    private lateinit var alarmManager:AlarmManager
    private lateinit var pendingIntent: PendingIntent
    lateinit var binding:FragmentUpcomingEventDetailsBinding
    lateinit var viewModel: UpcoEventDetailsModel
    lateinit var eventTitle:TextView
    lateinit var address:TextView
    lateinit var gdgName:TextView
    lateinit var dateAndTime:TextView
    lateinit var desc:TextView
    lateinit var progressBar: ProgressBar
    lateinit var scrollView:ScrollView
    lateinit var rsvp:TextView
    lateinit var organizerList:List<Organizers>
    lateinit var memberrecyclerView:RecyclerView
    lateinit var organizersAdapter: OrganizersAdapter
    lateinit var aboutcardView:CardView
    lateinit var membersCardView: CardView
    lateinit var notifyButton:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUpcomingEventDetailsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        progressBar=binding.progressBar
        scrollView=binding.scrollview

        progressBar.visibility=View.VISIBLE
        scrollView.visibility=View.GONE


        eventTitle=binding.eventsName
        address=binding.address
        gdgName=binding.gdgdName
        dateAndTime=binding.dateandtime
        desc=binding.desc
        rsvp=binding.rsvp
        memberrecyclerView=binding.memberRecyclerView
        aboutcardView=binding.aboutcardView
        membersCardView=binding.membersCardView
        notifyButton=binding.notifyButton



        organizerList=listOf()

        memberrecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        organizersAdapter= OrganizersAdapter(organizerList)
        memberrecyclerView.adapter=organizersAdapter




        return view
    }

    private fun setAlarm(dateAndTime:String) {

        alarmManager= activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent=Intent(requireContext(),AlarmReceiver::class.java)


        pendingIntent=PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val inputDateString = dateAndTime.dropLast(6)
        val inputFormat = "EEE, MMM dd, hh:mm a yyyy"
        val outputFormat = "yyyy-MM-dd hh:mm a"
        val year=LocalDateTime.now().year.toString()
        val inputDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        val outputDateFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
        val date = inputDateFormat.parse("$inputDateString $year")
        val formattedDate = outputDateFormat.format(date)
        val finaldate=outputDateFormat.parse(formattedDate)
        val calendar=Calendar.getInstance()
        calendar.setTime(finaldate)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        Log.d("Alarm","Alarm set for $date $formattedDate $finaldate")
//        ${formatter.parse("${dateAndTime.dropLast(16).drop(5)}$year")}


    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "GoogleCommunityVisulaToolChannel"
            val description = eventTitle.text
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description=description.toString()

            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val upcoEventDetailsRepo=upcoEventsDetailsRepo()

        viewModel=ViewModelProvider(requireActivity(),upcoeventDetailFactory(upcoEventDetailsRepo)).get(UpcoEventDetailsModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getResponseModel(url.upcomingeventsUrl,requireContext())
                Log.d("eventdetails","after getResponse")
            }
            Log.d("eventdetails","before fetching")
            delay(5000)
            withContext(Dispatchers.Main){
                if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE
                if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                val eventData= viewModel.returnEvents()
                gdgName.text=eventData.gdgName
                gdgName.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                eventTitle.text=eventData.title
                eventTitle.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                if(eventData.address.isEmpty()){
                    address.visibility=View.GONE
                }else{
                    address.text=eventData.address
                    address.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                }
                if(eventData.dateAndTime.isEmpty()){
                    dateAndTime.visibility=View.GONE
                }else{
                    dateAndTime.text=eventData.dateAndTime
                    dateAndTime.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                }
                desc.text=eventData.desc
                aboutcardView.visibility=View.VISIBLE
                aboutcardView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                if(eventData.rsvp.isEmpty()){
                    rsvp.visibility=View.GONE
                }else{
                    rsvp.text=eventData.rsvp
                    rsvp.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                }
                organizerList=eventData.mentors.toList()

                organizersAdapter.refreshData(organizerList)

                notifyButton.setOnClickListener {
                    createNotificationChannel()
                    setAlarm(eventData.dateAndTime)
                }
            }



        }


    }

}