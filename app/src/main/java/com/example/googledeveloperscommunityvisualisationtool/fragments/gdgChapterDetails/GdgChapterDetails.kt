package com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
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
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
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
    lateinit var noUpcomingEventTextView:TextView
    lateinit var starttourGdgButton:Button
    lateinit var stoptourGgdButton:Button
    lateinit var sharedPref:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var progressBar: ProgressBar
    lateinit var scrollView: ScrollView

     var handler=Handler()



     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=FragmentGdgChapterDetailsBinding.inflate(layoutInflater,container, false)
         val view=binding.root

         progressBar=binding.progressBar
         scrollView=binding.scrollView

         progressBar.visibility=View.VISIBLE
         scrollView.visibility=View.GONE


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

        Log.d("content","content visible")


        CoroutineScope(Dispatchers.IO).launch {
            getDetails()
        }
    }

    private suspend fun getDetails() {
        val job=CoroutineScope(Dispatchers.IO).launch {
            gdgViewModel.getCompleteGDGdetails(args.chapter.url)
//            sharedPref=activity?.getSharedPreferences(args.chapter.gdgName+"sharedpref",Context.MODE_PRIVATE)!!
//            editor=sharedPref.edit()
        }

//        if(sharedPref.contains(args.chapter.gdgName+"sharedpref")){
//            gdgName.text=sharedPref.getString("gdgname",null)
//            cityName.text=sharedPref.getString("cityname",null)
//            countryName.text=sharedPref.getString("countryname",null)
//            aboutGdg.text=sharedPref.getString("aboutgdg",null)
//            member.text=sharedPref.getString("member",null)
//            val organizerstring=sharedPref.getString("organzierslist",null)
//            val organizerstype= TypeToken<ArrayList<Organizers>>() {
//
//            }.type
//            organizerList=Gson().fromJson(organizerstring,organizerstype)



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

//            editor.apply{
//                putString("gdgname",gdgDetails.gdgName)
//                putString("cityname",gdgDetails.gdgName)
//                putString("countryname",gdgDetails.gdgName)
//                putString("aboutgdg",gdgDetails.gdgName)
//                putString("member",gdgDetails.gdgName)
//                val gson=Gson()
//                val stringorganizer=gson.toJson(organizerList)
//                val stringpastevent=gson.toJson(pastEventsList)
//                val stringupcoming=gson.toJson(upcomingEventlist)
//                putString("organzierslist",stringorganizer)
//                putString("organzierslist",stringpastevent)
//                putString("upcomingeventlist",stringupcoming)
//                apply()
//            }

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
            if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
            if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE



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