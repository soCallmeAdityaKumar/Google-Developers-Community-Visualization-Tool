package com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpcomingEvents : Fragment() {
    lateinit var binding:FragmentUpcomingEventsBinding
    lateinit var upcomingEventViewModel: UpcomingEventViewModel
     var eventlist= mutableListOf <Result>()
    lateinit var upcomingDatBaseViewModel: UpcomingEventdatabaseViewModel
    lateinit var adapter: UpcomingEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        binding=FragmentUpcomingEventsBinding.inflate(layoutInflater,container,false)
        val view= binding.root


        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter=UpcomingEventsAdapter(eventlist)
        binding.recyclerView.adapter = adapter


        binding.secondcardviewTextView.text="Upcoming Events"
        binding.thirdcardviewTextView.text="Last Week Events"

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.contentLoadingProgressBar.visibility=View.VISIBLE

        //ViewModel for fetching the events list
        val upcomingEventRepository= UpcomingEventRepository(requireContext())
        upcomingEventViewModel= ViewModelProvider(this, UpcomingEventViewModelFactory(upcomingEventRepository,requireContext()))
            .get(UpcomingEventViewModel::class.java)


        //ViewModel for Database
        upcomingDatBaseViewModel=ViewModelProvider(this,
            UpcomingEventRoomViewModelFactory(requireContext())
        )
            .get(UpcomingEventdatabaseViewModel::class.java)
        binding.contentLoadingProgressBar.visibility=View.VISIBLE


        checkDatabase()


    }


    private fun checkDatabase() {

        upcomingDatBaseViewModel.readAllEventViewModel.observe(viewLifecycleOwner, Observer {list->
            if(list.isEmpty()){
                networkCheckAndRun()
            }else{
                val events=convertDataType(list)
                adapter.refreshData(events)
                binding.contentLoadingProgressBar.visibility=View.GONE
                binding.recyclerView.visibility=View.VISIBLE

            }
        })

    }

    private fun convertDataType(list: List<UpcomingEventEntity>?): List<Result> {
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

    private fun networkCheckAndRun() {
        if(upcomingEventViewModel.isNetworkAvailable()){
            getAllUpcomingEvents()
        }else{
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllUpcomingEvents() {
        binding.contentLoadingProgressBar.visibility=View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("Inside the getAllUpcoEvet", Thread.currentThread().name)
                upcomingEventViewModel.getResponseViewModel()
                delay(5000)
                Log.d("after network call", "using recycler View")
                withContext(Dispatchers.Main) {
                    Log.d("Inside the getAllUpcoEvet for recycler", Thread.currentThread().name)
                    eventlist = upcomingEventViewModel.returnlistViewModel()
                }
                withContext(Dispatchers.Main) {
                    adapter.refreshData(eventlist)
                    binding.recyclerView.visibility=View.VISIBLE
                    binding.contentLoadingProgressBar.visibility=View.GONE
                }
                insertToDatabase()

            }
    }

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


}