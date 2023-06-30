package com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgChapModelFactory
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgScrapingRespo
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgViewModel
import com.example.googledeveloperscommunityvisualisationtool.dialog.CustomDialogUtility.showDialog
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.GDGDetails
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.PastEvents
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.UpcomingEvents
import com.example.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.example.googledeveloperscommunityvisualisationtool.create.utility.connection.LGConnectionTest.testPriorConnection
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentGdgChapterDetailsBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean


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
    lateinit var tourGDG:TourGDGThread
    lateinit var upcomingEventlist:List<events>
    lateinit var eventsAdapterpast: pastEventAdapter
    lateinit var upcoEventsAdapterupcoming: UpcoEventsAdapter
    lateinit var binding:FragmentGdgChapterDetailsBinding
    lateinit var gdgViewModel:GdgViewModel
    lateinit var gdgDetails: GDGDetails
    lateinit var member:TextView
    lateinit var organizerAdapter:OrganizersAdapter
    lateinit var organizerList:List<Organizers>
    lateinit var contentLoading:ProgressBar
    lateinit var noUpcomingEventTextView:TextView
    lateinit var starttourGdgButton:Button
    lateinit var stoptourGgdButton:Button
     var handler=Handler()



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
         upcoEventsAdapterupcoming=UpcoEventsAdapter(upcomingEventlist)
         upcomingEventsRecycler.adapter=upcoEventsAdapterupcoming
         upcomingEventsRecycler.visibility=View.GONE

         noUpcomingEventTextView=binding.NoUpcomingView
         noUpcomingEventTextView.visibility=View.VISIBLE

         pastEventsList= listOf()
         pasteventsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         eventsAdapterpast= pastEventAdapter(pastEventsList)
         pasteventsRecycler.adapter=eventsAdapterpast

         starttourGdgButton=binding.StarttourGdgButton
         stoptourGgdButton=binding.StoptourGdgButton
         starttourGdgButton.setOnClickListener { tour() }
         stoptourGgdButton.setOnClickListener { stopTour() }



        return view
    }

    private fun stopTour() {
        tourGDG!!.stop()
        starttourGdgButton!!.visibility = View.VISIBLE
        stoptourGgdButton!!.visibility = View.INVISIBLE
    }

    private fun tour() {
        val isConnected = AtomicBoolean(false)
        testPriorConnection(requireActivity(), isConnected)
        val sharedPreferences = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)
        handler.postDelayed({
            if (isConnected.get()) {
                showDialog(requireActivity(), "Starting the GDG TOUR")
                tourGDG = TourGDGThread(
                    args.chapter as ChapterEntity,
                    requireActivity(),
                    starttourGdgButton!!,
                    stoptourGgdButton!!
                )
                tourGDG!!.start()
                starttourGdgButton!!.visibility = View.INVISIBLE
                stoptourGgdButton!!.visibility = View.VISIBLE
            }
//            loadConnectionStatus(sharedPreferences)
        }, 1200)
    }
//    private fun loadConnectionStatus(sharedPreferences: SharedPreferences) {
//        val isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
//        if (isConnected) {
//            connectionStatus!!.background =
//                ContextCompat.getDrawable(applicationContext, R.drawable.ic_status_connection_green)
//        } else {
//            connectionStatus!!.background =
//                ContextCompat.getDrawable(applicationContext, R.drawable.ic_status_connection_red)
//        }
//    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gdgChapterRepo=GdgScrapingRespo()
        gdgViewModel = ViewModelProvider(this, GdgChapModelFactory(gdgChapterRepo, requireContext())).get(GdgViewModel::class.java)

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
            Log.d("upcomingeventinactivechapter",gdgDetails.upcomingEventsList.size.toString())
            if (upcomingEventlist.size!=0){
                upcoEventsAdapterupcoming.refreshData(upcomingEventlist)
                upcoEventsAdapterupcoming.setOnItemClickListener(object :UpcoEventsAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val uri= Uri.parse(upcomingEventlist[position].link)
                        val intent= Intent(Intent.ACTION_VIEW,uri)
                        startActivity(intent)
                    }

                })
                upcomingEventsRecycler.visibility=View.VISIBLE
                noUpcomingEventTextView.visibility=View.GONE
            }
            else{
                noUpcomingEventTextView.visibility=View.VISIBLE
                upcomingEventsRecycler.visibility=View.GONE
            }

            for(i in upcomingEventlist){
                Log.d("hello",i.toString())
            }
            eventsAdapterpast.refreshData(pastEventsList)

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