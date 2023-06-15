package com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Chapter
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents.UpcomingEventRepository
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents.UpcomingEventViewModel
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents.UpcomingEventViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventsBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventRoomViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventdatabaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpcomingEvents : Fragment() {
    lateinit var binding:FragmentUpcomingEventsBinding
    lateinit var upcomingDatBaseViewModel: UpcomingEventdatabaseViewModel
    lateinit var upcomingEventViewModel: UpcomingEventViewModel
      var eventlist= mutableListOf <Result>()
    lateinit var adapter: UpcomingEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        binding=FragmentUpcomingEventsBinding.inflate(layoutInflater,container,false)
        val view= binding.root

        binding.contentLoadingProgressBar.visibility=View.VISIBLE

        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter=UpcomingEventsAdapter(eventlist)
        binding.recyclerView.adapter = adapter


        binding.secondcardviewTextView.text="Upcoming Events"
        binding.thirdcardviewTextView.text="Last Week Events"

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //ViewModel for fetching the events list
        val upcomingEventRepository= UpcomingEventRepository(requireContext())
        upcomingEventViewModel= ViewModelProvider(this, UpcomingEventViewModelFactory(upcomingEventRepository,requireContext())).get(UpcomingEventViewModel::class.java)


        //ViewModel for Database
        upcomingDatBaseViewModel=ViewModelProvider(this, UpcomingEventRoomViewModelFactory(requireContext())).get(UpcomingEventdatabaseViewModel::class.java)
//        binding.contentLoadingProgressBar.visibility=View.VISIBLE


        checkDatabase()


    }


    //if database is empty it will fetch data from the api otherwise refresh the adapter
    private fun checkDatabase() {
            upcomingDatBaseViewModel.readAllEventViewModel.observe(viewLifecycleOwner, Observer {
                if (it.isEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        networkCheckAndRun()
                    }

                } else {
                    Log.d("events", "inside the checkdatabase else part")
                        val events = convertDataType(it)
                        adapter.refreshData(events)
                        binding.contentLoadingProgressBar.visibility=View.GONE
                        binding.recyclerView.visibility=View.VISIBLE

                }
            })


    }

    //Check For the Network:if available get the events
    private suspend fun networkCheckAndRun() {
        if(upcomingEventViewModel.isNetworkAvailable()){
            getAllUpcomingEvents()
        }else{
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getAllUpcomingEvents() {

        //getting the event from api

        val job=CoroutineScope(Dispatchers.Main).launch {

            val job2=CoroutineScope(Dispatchers.IO).launch {

                upcomingEventViewModel.getResponseViewModel()

            }

            job2.join()
            eventlist = upcomingEventViewModel.returnlistViewModel()
            adapter.refreshData(eventlist)
        }

        delay(5000)
            job.join()
        insertToDatabase()

        //Now insert into database

    }

    //insert into the database
    private  fun insertToDatabase() {

        Log.d("eventsizeininsertDatabase",eventlist.size.toString())
        for (events in eventlist){
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
                events.chapter.title,events.chapter.url,
                events.city,
                events.description_short,
                events.event_type_title,
                events.id,
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