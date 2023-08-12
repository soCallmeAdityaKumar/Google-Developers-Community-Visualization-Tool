package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.lastWeekEventDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentLastWeekEventDetailsBinding
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventModelFact
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventViewModel

class LastWeekEventDetails : Fragment() {
    val title:LastWeekEventDetailsArgs by navArgs()
   private lateinit var binding: FragmentLastWeekEventDetailsBinding
   lateinit var lastEventRoomViewModel: LastEventViewModel
   lateinit var gdgName: TextView
   lateinit var address:TextView
   lateinit var eventTitle:TextView
   lateinit var dateAndTime:TextView
   lateinit var desc:TextView
   lateinit var rsvp:TextView
    private var fragmentLifecycleOwner: LifecycleOwner?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLastWeekEventDetailsBinding.inflate(inflater,container,false)
        val view=binding.root

        fragmentLifecycleOwner=viewLifecycleOwner

        gdgName=binding.gdgdName
        address=binding.address
        eventTitle=binding.eventsName
        dateAndTime=binding.dateandtime
        desc=binding.desc
        rsvp=binding.rsvp



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lastEventRoomViewModel=
            ViewModelProvider(requireActivity(), LastEventModelFact(requireContext())).get(
                LastEventViewModel::class.java)

        lastEventRoomViewModel.readAllEventViewModel.observe(fragmentLifecycleOwner!!, Observer {

            for( event in it) {
                Log.d("lastWeekEventDetails","${title.eventTitle}==${event.title}")
                if(event.title.equals(title.eventTitle)) {
                    if (event.title.isNotEmpty()) {
                        eventTitle.text = event.title
                    }
                    if (event.gdgName.isNotEmpty()) {
                        gdgName.text = event.gdgName
                    }
                    if (event.rsvp.isNotEmpty()) {
                        rsvp.text = event.rsvp
                    }
                    if (event.dateAndTime.isNotEmpty()) {
                        dateAndTime.text = event.dateAndTime
                    }
                    if (event.desc.isNotEmpty()) {
                        desc.text = event.desc
                    }
                    if (event.address.isNotEmpty()) {
                        address.text = event.address
                    }
                }
            }

        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        lastEventRoomViewModel.readAllEventViewModel.removeObserver { fragmentLifecycleOwner }

    }
}