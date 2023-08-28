package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.app.AlarmManager
import android.app.Dialog

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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.dataItem.upcomEventData
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail.UpcoEventDetEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail.UpcoEventDetFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail.UpcoEventDetViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import kotlin.time.Duration.Companion.minutes

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
    lateinit var upcoEventDetViewModel: UpcoEventDetViewModel
    lateinit var eventData:upcomEventData
    private var fragmentLifecycleOwner: LifecycleOwner?=null
    lateinit var dialog:Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentLifecycleOwner=viewLifecycleOwner
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

    private fun setAlarm(dateAndTime:String,title:String,image:String,details:String,minusTime:Long) {
        Log.d("UpcomingEventDetails->setAlarm","inside setAlarm")
        Log.d("UpcomingEventDetails->setAlarm","minustime->$minusTime")

        var inputString=dateAndTime
        Log.d("UpcomingEventDetails->Alarm","Input String->${inputString.toString()}")
        var originalFormatter = ZonedDateTime.parse(inputString)
        Log.d("UpcomingEventDetails->Alarm","originalFormatter${originalFormatter.toString()}")
        originalFormatter=originalFormatter.minusMinutes(minusTime)
        Log.d("UpcomingEventDetails->Alarm","originalFormatter${originalFormatter.toString()}")
        alarmManager= activity?.getSystemService(ALARM_SERVICE) as AlarmManager

            val instant = originalFormatter.toInstant()
        instant.minusSeconds(minusTime*60)
            val date = Date.from(instant)
            Log.d("UpcomingEventDetails->Alarm","date->${date.toString()}")
            val calendar=Calendar.getInstance()
            calendar.setTime(date)
            Log.d("UpcomingEventDetails->Alarm","Calendar ${calendar.time.toString()}")

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
            Log.d("UpcomingEventDetails->Alarm","Alarm set for $date")
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
        upcoEventDetViewModel.readAllEventViewModel.removeObserver {  }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val upcoEventDetailsRepo=upcoEventsDetailsRepo()
        dialog=Dialog(requireContext())
        val dialogView=layoutInflater.inflate(R.layout.notification_option,null)
        dialog.setContentView(dialogView)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val sixHourRadio=dialog.findViewById<RadioButton>(R.id.sixhourRadio)
        val oneHourRadio=dialog.findViewById<RadioButton>(R.id.onehourRadio)
        val thirtyminRadio=dialog.findViewById<RadioButton>(R.id.thirtyminRadio)
        val sameTimeRadio=dialog.findViewById<RadioButton>(R.id.zeroHourRadio)
        viewModel=ViewModelProvider(requireActivity(),upcoeventDetailFactory(upcoEventDetailsRepo)).get(UpcoEventDetailsModel::class.java)
        upcoEventDetViewModel=ViewModelProvider(requireActivity(),UpcoEventDetFactory(requireContext())).get(UpcoEventDetViewModel::class.java)

        upcoEventDetViewModel.readAllEventViewModel.observe(fragmentLifecycleOwner!!, Observer {list->
            val foundElement=list.find { it.url==url.dateAndUrl.url}
            Log.d("UpcomingEventDetails","$foundElement")
            if(foundElement!=null){
                if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE
                if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                if(loadingAnimation.visibility==View.VISIBLE)loadingAnimation.visibility=View.GONE
                Log.d("UpcomingEventDetails","Event found in database and i not null $foundElement")
                gdgName.text=foundElement.gdgName
                if(isAdded) {
                    gdgName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                }

                eventTitle.text=foundElement.eventName
                if(isAdded) {
                    eventTitle.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                }

                if(foundElement.addresss.isEmpty()){
                    address.visibility=View.GONE
                }else{
                    address.text=foundElement.addresss
                    if(isAdded) {
                        address.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                android.R.anim.slide_in_left
                            )
                        )
                    }
                }
                if(foundElement.date.isEmpty()){
                    dateAndTime.visibility=View.GONE
                }else{
                    dateAndTime.text=foundElement.date
                    if(isAdded) {
                        dateAndTime.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                android.R.anim.slide_in_left
                            )
                        )
                    }
                }
                desc.text=foundElement.aboutEvent
                aboutcardView.visibility=View.VISIBLE
                if(isAdded) {
                    aboutcardView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                }

                if(foundElement.rsvp.isEmpty()){
                    rsvp.visibility=View.GONE
                }else{
                    rsvp.text=foundElement.rsvp
                    if(isAdded) {
                        rsvp.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                android.R.anim.slide_in_left
                            )
                        )
                    }
                }
                val mentorsString=foundElement.organizers
                val organizerlistType=object : TypeToken<List<Organizers>>() {}.type
                organizerList=Gson().fromJson(mentorsString,organizerlistType)
                organizersAdapter.refreshData(organizerList)

                notifyButton.setOnClickListener {
                    Log.d("UpcomingEventDetails", "notify buttton clicked")


                    dialog.show()
                    var radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
                    var confirmButton = dialogView.findViewById<AppCompatButton>(R.id.notificationButton)
                    var minusTime = 0
                    confirmButton.setOnClickListener {
                        var selectedRadioButtonId = radioGroup.checkedRadioButtonId
                        if (selectedRadioButtonId != -1) {
                            val selectedRadioButton =
                                dialogView.findViewById<RadioButton>(selectedRadioButtonId)
                            val selectedOption = selectedRadioButton.id.toString()
                            if (selectedOption == dialogView.findViewById<RadioButton>(R.id.sixhourRadio).id.toString()) {
                                minusTime = 6 * 60
                            } else if (selectedOption == dialogView.findViewById<RadioButton>(R.id.onehourRadio).id.toString()) {
                                minusTime = 60
                            } else if (selectedOption == dialogView.findViewById<RadioButton>(R.id.thirtyminRadio).id.toString()) {
                                minusTime = 30
                            }else if(selectedOption==dialogView.findViewById<RadioButton>(R.id.zeroHourRadio).id.toString()){
                                minusTime=0
                            }
                        }
                        createNotificationChannel()
                        setAlarm(
                            url.dateAndUrl.dateAndTime,
                            foundElement.eventName,
                            url.dateAndUrl.logo,
                            foundElement.aboutEvent,
                            minusTime.toLong()
                        )
                        dialog.cancel()
                    }
                }

            }else{
                Log.d("UpcomingEventDetails","Event Not found in database")

                CoroutineScope(Dispatchers.IO).launch {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.getResponseModel(url.dateAndUrl.url,requireContext())
                        Log.d("UpcomingEventDetails","after getResponse")
                    }
                    Log.d("UpcomingEventDetails","before fetching")
                    delay(5000)
                    withContext(Dispatchers.Main){
                        if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE
                        if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                        if(loadingAnimation.visibility==View.VISIBLE)loadingAnimation.visibility=View.GONE
                        eventData= viewModel.returnEvents()
                        storeinDatabase(eventData,url.dateAndUrl.url)
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
                            Log.d("UpcomingEventDetails","notify buttton clicked")


                            dialog.show()
                            var radioGroup=dialogView.findViewById<RadioGroup>(R.id.radioGroup)
                            var confirmButton=dialogView.findViewById<AppCompatButton>(R.id.notificationButton)
                            var minusTime=0
                            confirmButton.setOnClickListener {
                                var selectedRadioButtonId=radioGroup.checkedRadioButtonId
                                if(selectedRadioButtonId!=-1){
                                    val selectedRadioButton = dialogView.findViewById<RadioButton>(selectedRadioButtonId)
                                    val selectedOption = selectedRadioButton.id.toString()
                                    if(selectedOption==dialogView.findViewById<RadioButton>(R.id.sixhourRadio).id.toString()){
                                        minusTime=6*60
                                    }else  if(selectedOption==dialogView.findViewById<RadioButton>(R.id.onehourRadio).id.toString()){
                                        minusTime=60
                                    }
                                    else  if(selectedOption==dialogView.findViewById<RadioButton>(R.id.thirtyminRadio).id.toString()){
                                        minusTime=30
                                    }
                                    else  if(selectedOption==dialogView.findViewById<RadioButton>(R.id.zeroHourRadio).id.toString()){
                                        minusTime=0
                                    }
                                }
                                createNotificationChannel()
                                setAlarm(url.dateAndUrl.dateAndTime,eventData.title,url.dateAndUrl.logo,eventData.desc,minusTime.toLong())
                                dialog.cancel()
                            }
                        }


                    }
                }
            }
            })

    }

    private fun storeinDatabase(eventData:upcomEventData,url:String) {
        val organizerString= Gson().toJson(eventData.mentors)
        Log.d("UpcomingEventDetails","organizersString->$organizerString")
        upcoEventDetViewModel.addEventViewModel(UpcoEventDetEntity(0,eventData.title,eventData.address,url,eventData.rsvp,eventData.gdgName,eventData.dateAndTime,eventData.desc,organizerString))
        Log.d("UpcomingEventDetails","${eventData.title} added to database")

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