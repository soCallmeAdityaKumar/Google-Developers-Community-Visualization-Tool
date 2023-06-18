package com.example.googledeveloperscommunityvisualisationtool.Fragments.GDGChapterDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.DataBinderMapperImpl
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgChaptersViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgScrapingRespository
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgViewModel
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.GDGDetails
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.PastEvents
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.UpcomingEvents
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentGdgChapterDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text


class GdgChapterDetails : Fragment() {
    val args:GdgChapterDetailsArgs by navArgs()
    lateinit var gdgName:TextView
    lateinit var cityName:TextView
    lateinit var countryName:TextView
    lateinit var aboutGdg:TextView
    lateinit var organizerRecyclerView:RecyclerView
    lateinit var upcomingEventsRecycler:RecyclerView
    lateinit var pasteventsRecycler:RecyclerView
    lateinit var pastEventsList:List<events>
    lateinit var upcomingEventlist:List<events>
    lateinit var eventsAdapterpast: EventsAdapter
    lateinit var eventsAdapterupcoming: EventsAdapter
    lateinit var binding:FragmentGdgChapterDetailsBinding
    lateinit var gdgViewModel:GdgViewModel
    lateinit var gdgDetails: GDGDetails
    lateinit var member:TextView
    lateinit var organizerAdapter:OrganizersAdapter
    lateinit var organizerList:List<Organizers>
    lateinit var contentLoading:ProgressBar
    lateinit var noUpcomingEventTextView:TextView


     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=FragmentGdgChapterDetailsBinding.inflate(layoutInflater,container, false)
         val view=binding.root

         contentLoading=binding.contentLoadingProgressBar
         gdgName=binding.gdgname
         cityName=binding.cityname
         countryName=binding.countryname
         aboutGdg=binding.aboutgdg
         member=binding.memebers

         organizerRecyclerView=binding.orgainzersrecycler
         upcomingEventsRecycler=binding.upcomingrecyclerview
         pasteventsRecycler=binding.pasteventsrecycler

         organizerRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         organizerList= listOf()
         organizerAdapter= OrganizersAdapter(organizerList)
         organizerRecyclerView.adapter=organizerAdapter

         upcomingEventlist= listOf()
         upcomingEventsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         eventsAdapterupcoming=EventsAdapter(upcomingEventlist)
         upcomingEventsRecycler.adapter=eventsAdapterupcoming
         upcomingEventsRecycler.visibility=View.GONE

         noUpcomingEventTextView=binding.NoUpcomingView
         noUpcomingEventTextView.visibility=View.VISIBLE

         pastEventsList= listOf()
         pasteventsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         eventsAdapterpast= EventsAdapter(pastEventsList)
         pasteventsRecycler.adapter=eventsAdapterpast


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gdgChapterRepo=GdgScrapingRespository()
        gdgViewModel = ViewModelProvider(this, GdgChaptersViewModelFactory(gdgChapterRepo, requireContext())).get(GdgViewModel::class.java)

        contentLoading.visibility=View.VISIBLE
        Log.d("content","content visible")


        CoroutineScope(Dispatchers.IO).launch {
            getDetails()
        }
    }

    private suspend fun getDetails() {
        val job=CoroutineScope(Dispatchers.IO).launch {
            gdgViewModel.getCompleteGDGdetails(args.chapter.url)

        }
        job.join()
        withContext(Dispatchers.Main){
            gdgDetails=gdgViewModel.getdetails()
            gdgName.text=gdgDetails.gdgName
            cityName.text=args.chapter.city_name
            countryName.text=args.chapter.country
            aboutGdg.text=args.chapter.about
            member.text=args.chapter.membersNumber
            organizerList=gdgDetails.orgnaizersList
            pastEventsList=coneverttoeventsbypast(gdgDetails.pastEventsList)

            upcomingEventlist=coneverttoeventsbyupcoming(gdgDetails.upcomingEventsList)
            if (upcomingEventlist.size!=0){
                upcomingEventsRecycler.visibility=View.VISIBLE
                noUpcomingEventTextView.visibility=View.GONE
            }
            for(i in upcomingEventlist){
                Log.d("hello",i.toString())
            }
            eventsAdapterpast.refreshData(pastEventsList)
            eventsAdapterupcoming.refreshData(upcomingEventlist)
            organizerAdapter.refreshData(organizerList)
            contentLoading.visibility=View.GONE
        }


    }

    private fun coneverttoeventsbypast(pastEventsList: List<PastEvents>): List<events> {
        val events= mutableListOf<events>()
        for( i in pastEventsList){
            val event=events(i.pastEventstitle,i.pastEventsdate,i.pastEventslink,i.pastEventstype)
            events.add(event)
        }
        return events
    }
    private fun coneverttoeventsbyupcoming(upEventsList: List<UpcomingEvents>): List<events> {
        val events= mutableListOf<events>()
        for( i in upEventsList){
            val event=events(i.upcomingEventstitle,i.upcomingEventsdate,i.upcomingEventslink,i.upcomingEventsdescription)
            events.add(event)
        }
        return events
    }

}