package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldGdgOrganAdap
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventDetailsBinding
import com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.OrganizersAdapter
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class UpcomingEventDetails : Fragment() {
    val url:UpcomingEventDetailsArgs by navArgs()
    lateinit var binding:FragmentUpcomingEventDetailsBinding
    lateinit var viewModel: UpcoEventDetailsModel
    lateinit var eventTitle:TextView
    lateinit var address:TextView
    lateinit var gdgName:TextView
    lateinit var dateAndTime:TextView
    lateinit var desc:TextView
    lateinit var rsvp:TextView
    lateinit var organizerList:List<Organizers>
    lateinit var memberrecyclerView:RecyclerView
    lateinit var organizersAdapter: OrganizersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUpcomingEventDetailsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        eventTitle=binding.eventsName
        address=binding.address
        gdgName=binding.gdgdName
        dateAndTime=binding.dateandtime
        desc=binding.desc
        rsvp=binding.rsvp
        memberrecyclerView=binding.memberRecyclerView


        organizerList=listOf()

        memberrecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        organizersAdapter= OrganizersAdapter(organizerList)
        memberrecyclerView.adapter=organizersAdapter



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val upcoEventDetailsRepo=upcoEventsDetailsRepo()

        viewModel=ViewModelProvider(requireActivity(),upcoeventDetailFactory(upcoEventDetailsRepo)).get(UpcoEventDetailsModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getResponseModel(url.upcomingeventsUrl)
        }
    }

}