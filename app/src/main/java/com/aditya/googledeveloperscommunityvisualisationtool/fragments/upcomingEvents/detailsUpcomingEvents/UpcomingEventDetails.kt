package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.app.AlarmManager

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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.R.drawable
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldGdgOrganAdap
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.OrganizersAdapter
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.events
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.AlarmReceiver
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.UpcomingEvents
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventDetailsBinding
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.UpcomingEventDetailsArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class UpcomingEventDetails : Fragment() {
    val url: UpcomingEventDetailsArgs by navArgs()
    private  val notification=101
    private  val CHANNEL_ID="Upcoming_Event_Notification"
    private lateinit var calendar: Calendar
    private lateinit var alarmManager:AlarmManager
    private lateinit var pendingIntent: PendingIntent
    lateinit var binding: FragmentUpcomingEventDetailsBinding
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
    lateinit var notifyButton:Button
    lateinit var loadingAnimation:LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUpcomingEventDetailsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        progressBar=binding.progressBar
        scrollView=binding.scrollview
        loadingAnimation=binding.loadinLottieAnimation


        progressBar.visibility=View.GONE
        scrollView.visibility=View.GONE
        loadingAnimation.playAnimation()


        eventTitle=binding.eventsName
        address=binding.address
        gdgName=binding.gdgdName
        dateAndTime=binding.dateandtime
        desc=binding.desc
        rsvp=binding.rsvp
        memberrecyclerView=binding.memberRecyclerView
        aboutcardView=binding.aboutcardView
        notifyButton=binding.notifyButton



        organizerList=listOf()

        memberrecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        organizersAdapter= OrganizersAdapter(organizerList)
        memberrecyclerView.adapter=organizersAdapter




        return view
    }

    private fun setAlarm(dateAndTime:String,title:String,image:String,details:String) {

        alarmManager= activity?.getSystemService(ALARM_SERVICE) as AlarmManager

        val inputString=dateAndTime
        Log.d("inputString",inputString.toString())
        val originalFormatter = ZonedDateTime.parse(inputString)
        val instant = originalFormatter.toInstant()
        val date = Date.from(instant)
        Log.d("date",date.toString())
        val calendar=Calendar.getInstance()
        calendar.setTime(date)
        Log.d("calendar",calendar.time.toString())



        val intent=Intent(requireContext(),AlarmReceiver::class.java)
        intent.putExtra("title",title)
        intent.putExtra("image",image)
        intent.putExtra("desc",details)
        intent.putExtra("time",calendar.time.toString().dropLast(18))

        pendingIntent=PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        Log.d("Alarm","Alarm set for $date")
        Toast.makeText(requireContext(),"You will be notified for $title",Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val upcoEventDetailsRepo=upcoEventsDetailsRepo()

        viewModel=ViewModelProvider(requireActivity(),upcoeventDetailFactory(upcoEventDetailsRepo)).get(UpcoEventDetailsModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getResponseModel(url.dateAndUrl.url,requireContext())
                Log.d("eventdetails","after getResponse")
            }
            Log.d("eventdetails","before fetching")
            delay(5000)
            withContext(Dispatchers.Main){
                if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE
                if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                if(loadingAnimation.visibility==View.VISIBLE)loadingAnimation.visibility=View.GONE
                val eventData= viewModel.returnEvents()
                gdgName.text=eventData.gdgName
                if(isAdded) {
                    gdgName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                }

                eventTitle.text=eventData.title
                if(isAdded) {
                    eventTitle.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                }

                if(eventData.address.isEmpty()){
                    address.visibility=View.GONE
                }else{
                    address.text=eventData.address
                    if(isAdded) {
                        address.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                android.R.anim.slide_in_left
                            )
                        )
                    }
                }
                if(eventData.dateAndTime.isEmpty()){
                    dateAndTime.visibility=View.GONE
                }else{
                    dateAndTime.text=eventData.dateAndTime
                    if(isAdded) {
                        dateAndTime.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                android.R.anim.slide_in_left
                            )
                        )
                    }
                }
                desc.text=eventData.desc
                aboutcardView.visibility=View.VISIBLE
                if(isAdded) {
                    aboutcardView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                }

                if(eventData.rsvp.isEmpty()){
                    rsvp.visibility=View.GONE
                }else{
                    rsvp.text=eventData.rsvp
                    if(isAdded) {
                        rsvp.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                android.R.anim.slide_in_left
                            )
                        )
                    }
                }
                organizerList=eventData.mentors.toList()

                organizersAdapter.refreshData(organizerList)

                notifyButton.setOnClickListener {
                    createNotificationChannel()
                    setAlarm(url.dateAndUrl.dateAndTime,eventData.title,url.dateAndUrl.logo,eventData.desc)
                }
            }
        }
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
}