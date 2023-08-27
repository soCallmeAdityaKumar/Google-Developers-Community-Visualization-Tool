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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentLastWeekEventDetailsBinding
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.OrganizersAdapter
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventModelFact
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventViewModel
import com.airbnb.lottie.LottieAnimationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LastWeekEventDetails : Fragment() {
    val title:LastWeekEventDetailsArgs by navArgs()
   private lateinit var binding: FragmentLastWeekEventDetailsBinding
   lateinit var lastEventRoomViewModel: LastEventViewModel
   lateinit var gdgName: TextView
   lateinit var address:TextView
   lateinit var eventTitle:TextView
   lateinit var dateAndTime:TextView
   lateinit var organizerList:List<Organizers>
    lateinit var memberrecyclerView: RecyclerView
    lateinit var organizersAdapter: OrganizersAdapter
    lateinit var desc:TextView
   lateinit var rsvp:TextView
   private var fragmentLifecycleOwner: LifecycleOwner?=null
    lateinit var lotteAnimationView: LottieAnimationView

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
        memberrecyclerView=binding.memberRecyclerView
        lotteAnimationView=binding.loadinLottieAnimation

        lotteAnimationView.visibility=View.VISIBLE
        lotteAnimationView.playAnimation()

        organizerList=listOf()
        memberrecyclerView.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        organizersAdapter= OrganizersAdapter(organizerList)
        memberrecyclerView.adapter=organizersAdapter





        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lastEventRoomViewModel=
            ViewModelProvider(requireActivity(), LastEventModelFact(requireContext())).get(
                LastEventViewModel::class.java)

        lastEventRoomViewModel.readAllEventViewModel.observe(fragmentLifecycleOwner!!, Observer {list->

            val foundElement=list.find { it.eventName==title.eventTitle }
            if(foundElement!=null){
                lotteAnimationView.visibility=View.GONE
                if (foundElement.eventName.isNotEmpty()) {
                    eventTitle.text = foundElement.eventName
                }
                if (foundElement.gdgName.isNotEmpty()) {
                    gdgName.text = foundElement.gdgName
                }
                if (foundElement.rsvp.isNotEmpty()) {
                    rsvp.text = foundElement.rsvp
                }
                if (foundElement.date.isNotEmpty()) {
                    dateAndTime.text = foundElement.date
                }
                if (foundElement.aboutEvent.isNotEmpty()) {
                    desc.text = foundElement.aboutEvent
                    Log.d("LastWeekEventDetails","desc of event->${foundElement.aboutEvent}")
                }
                if (foundElement.addresss.isNotEmpty()) {
                    address.text = foundElement.addresss
                }
                if(foundElement.organizers.isNotEmpty()){
                    val organizersString=foundElement.organizers
                    val organizerlistType=object : TypeToken<List<Organizers>>() {}.type
                    organizerList= Gson().fromJson(organizersString,organizerlistType)
                    organizersAdapter.refreshData(organizerList)
                }
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        lastEventRoomViewModel.readAllEventViewModel.removeObserver { fragmentLifecycleOwner }

    }
}