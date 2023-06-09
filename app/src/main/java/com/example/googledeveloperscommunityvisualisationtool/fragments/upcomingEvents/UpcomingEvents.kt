package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.googledeveloperscommunityvisualisationtool.dataClass.volley.Chapter
import com.example.googledeveloperscommunityvisualisationtool.dataClass.volley.Result
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcomEventRepo
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcoEventViewMod
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcomingEventfactory
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventsBinding
import com.example.googledeveloperscommunityvisualisationtool.fragments.Calendar.CalendarFragment
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.DateAndUrl
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroomfactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroommodel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.weekEventEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcomingEventEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventRoomFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class UpcomingEvents : Fragment() {
    lateinit var binding:FragmentUpcomingEventsBinding
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
    lateinit var progressBar: ProgressBar
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var lastweekroomViewModel:lastweekroommodel
    val listOfDates= mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {

        binding=FragmentUpcomingEventsBinding.inflate(layoutInflater,container,false)
        val view= binding.root

        refreshLayout=binding.refreshlayout

        progressBar=binding.progressBar
        progressBar.visibility=View.VISIBLE


        secondcardViewTextView=binding.secondcardviewTextView
        thirdCardViewTextView=binding.thirdcardviewTextView
        upcomingRecyclerView=binding.recyclerView
        lastweekRecyclerView=binding.lastweekRecyclerview



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


        //ViewModel for Database
        upcomingDatBaseViewModel=ViewModelProvider(this, UpcoEventRoomFactory(requireContext())).get(UpcoEventroomViewmodel::class.java)
//        binding.contentLoadingProgressBar.visibility=View.VISIBLE


        lastweekroomViewModel=ViewModelProvider(this,lastweekroomfactory(requireContext())).get(lastweekroommodel::class.java)


        checkDatabase()


    }

    override fun onDestroy() {
        lastweekroomViewModel.readlastweekEventViewModel.removeObserver{}
        upcomingDatBaseViewModel.readAllEventViewModel.removeObserver{}
        super.onDestroy()

    }


    //if database is empty it will fetch data from the api otherwise refresh the adapter
    private fun checkDatabase() {
            upcomingDatBaseViewModel.readAllEventViewModel.observe(requireActivity(), Observer {
                if (it.isEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        networkCheckAndRun()
                    }
                } else {
                    Log.d("events", "inside the checkdatabase else part")
                    if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                    if(secondcardViewTextView.visibility==View.GONE)secondcardViewTextView.visibility=View.VISIBLE
                    if(upcomingRecyclerView.visibility==View.GONE)upcomingRecyclerView.visibility=View.VISIBLE
                    secondcardViewTextView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                    upcomingRecyclerView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))


                    eventlist = convertDataType(it).toMutableList()
                    adapter.refreshData(eventlist)
                    refreshLayout.setOnRefreshListener {
                        checkexistingevent(it.toMutableList())
                        refreshLayout.isRefreshing=false
                    }
                    adapter.setOnItemClickListener(object:UpcoEventsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val action=UpcomingEventsDirections.actionUpcomingEventsToUpcomingEventDetails(DateAndUrl(eventlist[position].url,eventlist[position].start_date))
                            findNavController().navigate(action)
                        }
                    })

                }
            })

        lastweekroomViewModel.readlastweekEventViewModel.observe(requireActivity(), Observer {
            if(it.isNotEmpty()){
                Log.d("upcomingevent","fragment created database size ${it.size} ")
                val result=convertweekevententityToResult(it)
                if(thirdCardViewTextView.visibility==View.GONE)thirdCardViewTextView.visibility=View.VISIBLE
                if(lastweekRecyclerView.visibility==View.GONE)lastweekRecyclerView.visibility=View.VISIBLE
                thirdCardViewTextView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                lastweekRecyclerView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                lastweekadapter.refreshData(result)
                lastweekadapter.setOnItemClickListener(object :UpcoEventsAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
//                        val action=UpcomingEventsDirections.actionUpcomingEventsToUpcomingEventDetails(lastweekeventslist[position].url)
//                        findNavController().navigate(action)
                    }
                })
            }
        })


    }

    private fun checkexistingevent(events: MutableList<UpcomingEventEntity>) {
        val currentDate=LocalDateTime.now().atZone(ZoneId.systemDefault())
        for(i in 0 until events.size){
            val eventDate=LocalDateTime.parse(events[i].start_date.dropLast(6)).atZone(ZoneId.systemDefault())
            val lastweekdate=currentDate.minusSeconds(604800)
            if(lastweekdate<eventDate && eventDate<currentDate){

                Log.d("date","id->${events[i].title},Eventdate->${eventDate},currentdate->${currentDate},lastweekdate->${lastweekdate}")

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


                Log.d("lastweek","${events[i].title} added to database")

                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    lastweekroomViewModel.readlastweekEventViewModel.observe(requireActivity(),
                        Observer {
                            if(it.isNotEmpty()){
                                Log.d("lastweek","database size ${it.size}")
                                val result=convertweekevententityToResult(it)
                                if(thirdCardViewTextView.visibility==View.GONE)thirdCardViewTextView.visibility=View.VISIBLE
                                if(lastweekRecyclerView.visibility==View.GONE)lastweekRecyclerView.visibility=View.VISIBLE
                                lastweekadapter.refreshData(result)
                                Log.d("lastweek","lastweekeventslist size->${result.size}")
                            }
                        })
                }
            }
        }
        Log.d("lastweek","before deleting the database")
        upcomingDatBaseViewModel.deleteAllevent()
        checkDatabase()
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
            getAllUpcomingEvents()
        }else{
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getAllUpcomingEvents() {

        //getting the event from api
        withContext(Dispatchers.Main) {
            if (progressBar.visibility == View.GONE) progressBar.visibility = View.VISIBLE
        }
        val job=CoroutineScope(Dispatchers.Main).launch {

            val job2=CoroutineScope(Dispatchers.IO).launch {

                upcoEventViewMod.getResponseViewModel()

            }


            job2.join()
            eventlist = upcoEventViewMod.returnlistViewModel()
            Log.d("eventlistsize",eventlist.size.toString())



            withContext(Dispatchers.Main){
                adapter.refreshData(eventlist)
            }
        }

        delay(5000)
            job.join()
        Log.d("upcomingevent","before inserttodatabase")
        insertToDatabase()
        withContext(Dispatchers.Main){
            checkDatabase()

        }

        //Now insert into database

    }

    //insert into the database
    private  fun insertToDatabase() {

        Log.d("eventsizeininsertDatabase",eventlist.size.toString())

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
        Log.d("events","inside the convert  part")

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