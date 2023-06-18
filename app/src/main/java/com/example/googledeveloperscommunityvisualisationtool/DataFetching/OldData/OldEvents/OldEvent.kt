package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents.OldEventsDataClass.Event
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents.OldEventsDataClass.Organizer
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents.OldEventsDataClass.oldEventsData
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentOldEventBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OldEvent : Fragment() {
    val endpoint:OldEventArgs by navArgs()
    lateinit var binding: FragmentOldEventBinding
    lateinit var oldEventViewModel: OldEventViewModel
    lateinit var pasteventRecyclerView:RecyclerView
    lateinit var organizerrecyclerView:RecyclerView
    lateinit var gdgName:TextView
    lateinit var cityName:TextView
    lateinit var countryname:TextView
    lateinit var eventList:List<Event>
    lateinit var organizerList: List<Organizer>
    lateinit var EventAdapter:oldEventAdapter
    lateinit var organizerAdapter:oldgdgorganizerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOldEventBinding.inflate(inflater,container,false)
        val view=binding.root

        gdgName=binding.gdgName
        gdgName.text=endpoint.chaptername.name
        cityName=binding.cityname
        cityName.text=endpoint.chaptername.city
        countryname=binding.countryname
        countryname.text=endpoint.chaptername.country

        eventList= listOf()
        pasteventRecyclerView=binding.eventRecyclerView
        pasteventRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        EventAdapter= oldEventAdapter(eventList)
        pasteventRecyclerView.adapter=EventAdapter


        organizerList= listOf()
        organizerrecyclerView=binding.OrganizersRecyclerView
        organizerrecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        organizerAdapter= oldgdgorganizerAdapter(organizerList)
        organizerrecyclerView.adapter=organizerAdapter

        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val oldEventRepository=OldEventRepository(requireContext())
        oldEventViewModel=ViewModelProvider(this,OldEventViewfactory(oldEventRepository,requireContext())).get(OldEventViewModel::class.java)

        val job=CoroutineScope(Dispatchers.IO).launch {
            oldEventViewModel.getResponse(endpoint.chaptername.urlname)
            Log.d("oldevent","reponse sent")
        }
        CoroutineScope(Dispatchers.IO).launch {
            job.join()
            delay(4000)
            Log.d("oldevent","after response")
            withContext(Dispatchers.Main){
                var gdgList=oldEventViewModel.getOldEventsViewModel()
                eventList=gdgList.events
                EventAdapter.refreshdata(eventList)
                Log.d("oldevent",eventList.size.toString())
                organizerList=gdgList.organizers
                Log.d("oldevent",organizerList.size.toString())
                organizerAdapter.refreshdata(organizerList)
            }
        }

    }


}