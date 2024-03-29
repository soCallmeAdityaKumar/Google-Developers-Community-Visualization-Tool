package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.dataClass.volley.Chapter
import com.aditya.googledeveloperscommunityvisualisationtool.dataClass.volley.Result
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcomEventRepo
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcoEventViewMod
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcomingEventfactory
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.DateAndUrl
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.UpcoEventDetailsModel
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.upcoEventsDetailsRepo
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.upcoeventDetailFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroomfactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroommodel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.weekEventEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventModelFact
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventRoomFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventsBinding
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail.UpcoEventDetFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail.UpcoEventDetViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId

class UpcomingEvents : Fragment() {
    lateinit var binding: FragmentUpcomingEventsBinding
    lateinit var upcomingDatBaseViewModel: UpcoEventroomViewmodel
    lateinit var upcoEventViewMod: UpcoEventViewMod
    var eventlist= mutableListOf <Result>()
    lateinit var lastweekRecyclerView: RecyclerView
    lateinit var lastweekadapter:UpcoEventsAdapter
     var lastweekeventslist= mutableListOf<Result>()
    lateinit var adapter: UpcoEventsAdapter
    lateinit var secondcardViewTextView:TextView
    lateinit var thirdCardViewTextView: TextView
    lateinit var upcomingRecyclerView:RecyclerView
    lateinit var scrollView:ScrollView
    lateinit var lastEventViewModel:LastEventViewModel
    lateinit var upcomingEventViewModel:UpcoEventDetailsModel
    lateinit var lastweekroomViewModel:lastweekroommodel
    lateinit var lastWeekEventDetViewModel:LastEventViewModel
    private var fragmentLifecycleOwner:LifecycleOwner?=null
    lateinit var loadingAnimation:LottieAnimationView
    lateinit var upcoEventDetViewModel: UpcoEventDetViewModel
    lateinit var refreshButton:AppCompatButton
    val listOfDates= mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        fragmentLifecycleOwner=viewLifecycleOwner

        binding=FragmentUpcomingEventsBinding.inflate(layoutInflater,container,false)
        val view= binding.root


//        progressBar=binding.progressBar
//        progressBar.visibility=View.VISIBLE
        loadingAnimation=binding.loadinLottieAnimation
        loadingAnimation.visibility=View.VISIBLE
        loadingAnimation.playAnimation()


        secondcardViewTextView=binding.secondcardviewTextView
        thirdCardViewTextView=binding.thirdcardviewTextView
        upcomingRecyclerView=binding.recyclerView
        lastweekRecyclerView=binding.lastweekRecyclerview
        refreshButton=binding.RefreshButton
        scrollView=binding.scrollView



        secondcardViewTextView.visibility=View.GONE
        thirdCardViewTextView.visibility=View.GONE
        upcomingRecyclerView.visibility=View.GONE
        lastweekRecyclerView.visibility=View.GONE

        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter=UpcoEventsAdapter(eventlist)
        binding.recyclerView.adapter = adapter


        binding.secondcardviewTextView.text="Upcoming Events"
        binding.thirdcardviewTextView.text="Last Week Events"


//        lastweekeventslist= mutableListOf()
        lastweekRecyclerView.visibility=View.GONE
        lastweekRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        lastweekadapter=UpcoEventsAdapter(lastweekeventslist)
        lastweekRecyclerView.adapter=lastweekadapter


        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //ViewModel for fetching the events list
        val upcomEventRepo= UpcomEventRepo(requireContext())
        upcoEventViewMod= ViewModelProvider(this, UpcomingEventfactory(upcomEventRepo,requireContext())).get(UpcoEventViewMod::class.java)

        val upcoEventDetailsRepo= upcoEventsDetailsRepo()
        upcomingEventViewModel=ViewModelProvider(requireActivity(), upcoeventDetailFactory(upcoEventDetailsRepo)).get(
            UpcoEventDetailsModel::class.java)

        //ViewModel for Database
        upcomingDatBaseViewModel=ViewModelProvider(this, UpcoEventRoomFactory(requireContext())).get(UpcoEventroomViewmodel::class.java)
//        binding.contentLoadingProgressBar.visibility=View.VISIBLE


        lastweekroomViewModel=ViewModelProvider(this,lastweekroomfactory(requireContext())).get(lastweekroommodel::class.java)

        lastEventViewModel=ViewModelProvider(this,LastEventModelFact(requireContext())).get(LastEventViewModel::class.java)

        upcoEventDetViewModel=ViewModelProvider(this,UpcoEventDetFactory(requireContext())).get(UpcoEventDetViewModel::class.java)

        lastWeekEventDetViewModel=ViewModelProvider(this,LastEventModelFact(requireContext())).get(LastEventViewModel::class.java)
        checkDatabase()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        lastweekroomViewModel.readlastweekEventViewModel.removeObserver{fragmentLifecycleOwner}
        upcomingDatBaseViewModel.readAllEventViewModel.removeObserver{fragmentLifecycleOwner}

    }


    //if database is empty it will fetch data from the api otherwise refresh the adapter
    private fun checkDatabase() {
        Log.d("UpcomingEvents->CheckDatabase()", "inside the checkdatabase ")

        upcomingDatBaseViewModel.readAllEventViewModel.observe(fragmentLifecycleOwner!!, Observer {it->
                if (it.isEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("UpcomingEvents->CheckDatabase()", "before network check")
                        networkCheckAndRun()
                    }
                } else {
                    Log.d("UpcomingEvents->CheckDatabase()", "inside the checkdatabase else part")
                    if(loadingAnimation.visibility==View.VISIBLE)loadingAnimation.visibility=View.GONE
                    if(secondcardViewTextView.visibility==View.GONE)secondcardViewTextView.visibility=View.VISIBLE
                    if(upcomingRecyclerView.visibility==View.GONE)upcomingRecyclerView.visibility=View.VISIBLE
                    secondcardViewTextView.startAnimation(AnimationUtils.loadAnimation(binding.root.context,android.R.anim.slide_in_left))
                    upcomingRecyclerView.startAnimation(AnimationUtils.loadAnimation(binding.root.context,android.R.anim.slide_in_left))


                    eventlist = convertDataType(it).toMutableList()
                    adapter.refreshData(eventlist)
                    refreshButton.setOnClickListener {view->
                        checkexistingevent(it.toMutableList())

                    }

                    adapter.setOnItemClickListener(object:UpcoEventsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val action= UpcomingEventsDirections.actionUpcomingEventsToUpcomingEventDetails(DateAndUrl(eventlist[position].url,eventlist[position].start_date,
                                eventlist[position].picture.thumbnail_url!!
                            ))
                            findNavController().navigate(action)
                        }
                    })

                }
            })

        lastweekroomViewModel.readlastweekEventViewModel.observe(fragmentLifecycleOwner!!, Observer {
            if(it.isNotEmpty()){
                Log.d("UpcomingEvents->CheckDatabase()","fragment created database size ${it.size} ")
                val result=convertweekevententityToResult(it)
                if(thirdCardViewTextView.visibility==View.GONE)thirdCardViewTextView.visibility=View.VISIBLE
                if(lastweekRecyclerView.visibility==View.GONE)lastweekRecyclerView.visibility=View.VISIBLE
                thirdCardViewTextView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                lastweekRecyclerView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                lastweekadapter.refreshData(result)
                lastweekadapter.setOnItemClickListener(object :UpcoEventsAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val action=UpcomingEventsDirections.actionUpcomingEventsToLastWeekEventDetails(result[position].title)
                        findNavController().navigate(action)
                    }
                })
            }
        })


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
            menuButton.setOnClickListener {
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

    private fun checkexistingevent(events: MutableList<UpcomingEventEntity>) {
        Log.d("UpcomingEvents->checkexistingevent()", "inside checkexistingevent()")
        scrollView.visibility=View.GONE
        loadingAnimation.visibility=View.VISIBLE
        loadingAnimation.playAnimation()
        val currentDate=LocalDateTime.now().atZone(ZoneId.systemDefault())
        for(i in 0 until events.size){
            val eventDate=LocalDateTime.parse(events[i].start_date.dropLast(6)).atZone(ZoneId.systemDefault())
            val lastweekdate=currentDate.minusSeconds(604800)
            if(lastweekdate<eventDate && eventDate<currentDate){

                Log.d("UpcomingEvents->CheckExistingEvent","id->${events[i].title},Eventdate->${eventDate},currentdate->${currentDate},lastweekdate->${lastweekdate}")

                lastweekroomViewModel.addEventViewModel(weekEventEntity(
                    events[i].Chapter_location,
                    events[i].CityName,
                    events[i].Country,
                    events[i].Country_name,
                    events[i].Description,
                    events[i].ChapterId,
                    events[i].Relative_url,
                    events[i].State,
                    events[i].Timezone,
                    events[i].ChapterTitle,
                    events[i].ChapterUrl,
                    events[i].city,
                    events[i].description_short,
                    events[i].event_type_title,
                    events[i].id,
                    events[i].picture,
                    events[i].start_date,
                    events[i].tags,
                    events[i].title,
                    events[i].url))


                upcoEventDetViewModel.readAllEventViewModel.observe(fragmentLifecycleOwner!!,
                    Observer { list->
                        val foundElement=list.find { it.url==events[i].url }

                        if(foundElement!=null){
                            lastWeekEventDetViewModel.addEventViewModel(LastEventEntity(0,foundElement.eventName,foundElement.addresss,foundElement.url,foundElement.rsvp,foundElement.gdgName,foundElement.date,foundElement.aboutEvent,foundElement.organizers))
                            Log.d("UpcomingEvents","${foundElement.eventName}  event added to Database was found")
                        }else{
                            CoroutineScope(Dispatchers.IO).launch {
                                CoroutineScope(Dispatchers.IO).launch {
                                    upcomingEventViewModel.getResponseModel(events[i].url,requireContext())

                                }
                                delay(5000)
                                val event=upcomingEventViewModel.returnEvents()
                                val organizersString= Gson().toJson(event.mentors)
                                lastWeekEventDetViewModel.addEventViewModel(LastEventEntity(0,event.title,event.address,events[i].url,event.rsvp,event.gdgName,event.dateAndTime,event.desc,organizersString))
                                Log.d("UpcomingEvents","${foundElement?.eventName}  event added to Database was found")
                            }
                        }
                    })
                Log.d("UpcomingEvents","last week ->${events[i].title} added to database")



            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            withContext(Dispatchers.Main){
                lastweekroomViewModel.readlastweekEventViewModel.observe(fragmentLifecycleOwner!!,
                    Observer {
                        if(it.isNotEmpty()){
                            Log.d("UpcomingEvents","lastweek database size ${it.size}")
                            val result=convertweekevententityToResult(it)
                            if(thirdCardViewTextView.visibility==View.GONE)thirdCardViewTextView.visibility=View.VISIBLE
                            if(lastweekRecyclerView.visibility==View.GONE)lastweekRecyclerView.visibility=View.VISIBLE
                            lastweekadapter.refreshData(result)
                            Log.d("UpcomingEvents","lastweekeventslist size->${result.size}")
                        }
                    })}
        }
        Log.d("UpcomingEvents","before deleting the database")
        upcomingDatBaseViewModel.deleteAllevent()
        Log.d("UpcomingEvents","after deleting the database")
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                checkDatabase()
                scrollView.visibility = View.VISIBLE
                loadingAnimation.visibility = View.GONE
            }
        }
    }



    fun convertweekevententityToResult(weekEventEntity: List<weekEventEntity>):List<Result>{
        val result= mutableListOf<Result>()
        for(k in 0 until weekEventEntity.size){
            val tags= mutableListOf<String>()
            var string=""
            for(j in 0 until weekEventEntity[k].tags.length){
                if(weekEventEntity[k].tags[j]==' '){
                    tags.add(string)
                    string =""
                }else{
                    string+=weekEventEntity[k].tags[j]
                }
            }
            result.add(
                Result(
                    Chapter(weekEventEntity[k].Chapter_location,weekEventEntity[k].city,weekEventEntity[k].Country,weekEventEntity[k].Country_name,weekEventEntity[k].description_short,weekEventEntity[k].id,weekEventEntity[k].Relative_url,weekEventEntity[k].State,weekEventEntity[k].Timezone,weekEventEntity[k].ChapterTitle,weekEventEntity[k].url),
                    weekEventEntity[k].city,weekEventEntity[k].Description,weekEventEntity[k].event_type_title,weekEventEntity[k].id,weekEventEntity[k].picture,weekEventEntity[k].start_date,tags,weekEventEntity[k].title,weekEventEntity[k].url)
            )
        }
        return result.toList()
    }

    //Check For the Network:if available get the events
    private suspend fun networkCheckAndRun() {
        if(upcoEventViewMod.isNetworkAvailable()){
            Log.d("UpcomingEvents","network Available")
            getAllUpcomingEvents()
        }else{
            Toast.makeText(binding.root.context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getAllUpcomingEvents() {
        Log.d("UpcomingEvents","inside getAllUpcomingEvents()")

        //getting the event from api
        withContext(Dispatchers.Main) {
            if(loadingAnimation.visibility==View.GONE)loadingAnimation.visibility=View.VISIBLE
            loadingAnimation.playAnimation()

        }
        val job=CoroutineScope(Dispatchers.Main).launch {
            val job2=CoroutineScope(Dispatchers.IO).launch {
                upcoEventViewMod.getResponseViewModel()
                Log.d("UpcomingEvents","events got")
            }
            job2.join()
            eventlist = upcoEventViewMod.returnlistViewModel()
            Log.d("UpcomingEvents", "eventList size ->${eventlist.size.toString()}")

            withContext(Dispatchers.Main){
                adapter.refreshData(eventlist)
            }
        }

        delay(5000)
            job.join()
        Log.d("UpcomingEvents","before inserttodatabase")
        insertToDatabase()
        withContext(Dispatchers.Main){
            checkDatabase()

        }

        //Now insert into database

    }

    //insert into the database
    private  fun insertToDatabase() {

        Log.d("UpcomingEvents->InsertDatabase","insertDatabase->${eventlist.size.toString()}")

        for (events in eventlist){

            listOfDates.add(events.start_date)

            var alltags=""
            for (eventstags in events.tags){
                alltags+="$eventstags "
            }
            val upcomingEvents= UpcomingEventEntity(
                events.chapter.chapter_location,
                events.chapter.city,
                events.chapter.country,
                events.chapter.country_name,
                events.chapter.description,
                events.chapter.id,
                events.chapter.relative_url,
                events.chapter.state,
                events.chapter.timezone,
                events.chapter.title,
                events.chapter.url,
                events.city,
                events.description_short,
                events.event_type_title,
                events.id,
                events.picture,
                events.start_date,
                alltags,
                events.title,
                events.url
            )
            upcomingDatBaseViewModel.addEventViewModel(upcomingEvents)
        }
    }



    private fun convertDataType(list: List<UpcomingEventEntity>?): List<Result> {
        Log.d("UpcomingEvents->ConvertDataTypes","inside the convert  part")

        val eventList= mutableListOf<Result>()
        if (list != null) {
            for(event in list){
                val tags= mutableListOf<String>()
                var string=""
                for(i in 0 until event.tags.length){
                    if(event.tags[i]==' '){
                        tags.add(string)
                        string =""
                    }else{
                        string+=event.tags[i]
                    }
                }
                val eventlist=Result(
                    Chapter(
                        event.Chapter_location,
                        event.CityName,
                        event.Country,
                        event.Country_name,
                        event.Description,
                        event.ChapterId,
                        event.Relative_url,
                        event.State,
                        event.Timezone,
                        event.ChapterTitle,
                        event.ChapterUrl),
                    event.city,
                    event.description_short,
                    event.event_type_title,
                    event.id,
                    event.picture,
                    event.start_date,
                    tags,
                    event.title,
                    event.url
                )
            eventList.add(eventlist)
            }
        }
        return eventList
    }

}