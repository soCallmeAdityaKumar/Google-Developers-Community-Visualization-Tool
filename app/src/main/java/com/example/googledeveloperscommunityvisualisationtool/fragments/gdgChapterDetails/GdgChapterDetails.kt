package com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.CalendarContract.Events
import android.util.Log
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.R
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
import com.example.googledeveloperscommunityvisualisationtool.databinding.ActivityMainBinding
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
    lateinit var aboutgdgcardView:CardView
    lateinit var organizercardView:CardView
    lateinit var upcomingcardView:CardView
    lateinit var pasteventCardView:CardView

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
         pasteventCardView=binding.pasteventscardview
         upcomingcardView=binding.upcomingcardView
         aboutgdgcardView=binding.aboutgdgcardview
         organizercardView=binding.organizercardview


         gdgName.visibility=View.GONE
         cityName.visibility=View.GONE
         countryName.visibility=View.GONE
         member.visibility=View.GONE
         pasteventCardView.visibility=View.GONE
         upcomingcardView.visibility=View.GONE
         aboutgdgcardView.visibility=View.GONE
         organizercardView.visibility=View.GONE


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
            loadConnectionStatus(sharedPreferences!!)
        }, 1200)
    }
    private fun loadConnectionStatus(sharedPreferences: SharedPreferences) {
        val isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        val act=activity as MainActivity
        if (isConnected) {
            act.binding.appBarMain.connectionStatus.text="Connected"
        } else {
            act.binding.appBarMain.connectionStatus.text="Not Connected"
        }
    }


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
        val store=(activity as MainActivity)
        val gson = Gson()
        sharedPref=activity?.getSharedPreferences(args.chapter.gdgName,Context.MODE_PRIVATE)!!
        editor=sharedPref.edit()
        val job=CoroutineScope(Dispatchers.IO).launch {
            gdgViewModel.getCompleteGDGdetails(args.chapter.url)
        }

        if(sharedPref.getString("gdgname",null)==args.chapter.gdgName) {
            Log.d("storedargs",args.chapter.gdgName)
            Log.d("stored",sharedPref.getString("gdgname",null).toString())
            gdgName.text = sharedPref.getString("gdgname", null)
            cityName.text = sharedPref.getString("cityname", null)
            countryName.text = sharedPref.getString("countryname", null)
            aboutGdg.text = sharedPref.getString("aboutgdg", null)
            member.text = sharedPref.getString("member", null)
            val orgaizersString= sharedPref.getString("organzierslist",null)
            val pasteventsString= sharedPref.getString("pasteventslist",null)!!.replace(Regex("&quot;"),"")
            val upcomingeventsString= sharedPref.getString("upcomingeventlist",null)


            val organizerlistType=object :TypeToken<List<Organizers>>() {}.type
            val eventlistType=object :TypeToken<List<events>>() {}.type

            pastEventsList=gson.fromJson(pasteventsString,eventlistType)
            upcomingEventlist=gson.fromJson(upcomingeventsString,eventlistType)

            organizerList=gson.fromJson(orgaizersString,organizerlistType)

            Log.d("stored",pastEventsList.size.toString())
            Log.d("stored",pastEventsList[0].toString())

            organizerAdapter.refreshData(organizerList)
            eventsAdapterpast.refreshData(pastEventsList)

            //make views visible
            withContext(Dispatchers.Main) {
                gdgName.visibility = View.VISIBLE
                gdgName.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                cityName.visibility = View.VISIBLE
                cityName.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                countryName.visibility = View.VISIBLE
                countryName.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                member.visibility = View.VISIBLE
                member.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                pasteventCardView.visibility = View.VISIBLE
                pasteventCardView.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                upcomingcardView.visibility = View.VISIBLE
                upcomingcardView.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                aboutgdgcardView.visibility = View.VISIBLE
                aboutgdgcardView.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                organizercardView.visibility = View.VISIBLE
                organizercardView.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    ),
                )
            }






            withContext(Dispatchers.Main){
                if(upcomingEventlist.size!=0){
                    upcoEventsAdapterupcoming.refreshData(upcomingEventlist)
                    upcoEventsAdapterupcoming.setOnItemClickListener(object :
                        UpcoEventsAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val uri = Uri.parse(upcomingEventlist[position].link)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }

                    })
                    upcomingEventsRecycler.visibility = View.VISIBLE
                    noUpcomingEventTextView.visibility = View.GONE
                }else{
                    noUpcomingEventTextView.visibility = View.VISIBLE
                    upcomingEventsRecycler.visibility = View.GONE
                }
            if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
            if (scrollView.visibility == View.GONE) {
                scrollView.visibility = View.VISIBLE
            }
            }
        }
        else {
            job.join()
            withContext(Dispatchers.Main) {

                gdgDetails = gdgViewModel.getdetails()
                gdgName.text = gdgDetails.gdgName
                cityName.text = args.chapter.city_name
                countryName.text = args.chapter.country
                aboutGdg.text = args.chapter.about
                member.text = args.chapter.membersNumber
                organizerList = gdgDetails.orgnaizersList
                pastEventsList = coneverttoeventsbypast(gdgDetails.pastEventsList)
                upcomingEventlist = coneverttoeventsbyupcoming(gdgDetails.upcomingEventsList)


                if (store.storedgdgData<2) {
                    store.storedgdgData+=1
                    editor.apply {
                        putString("gdgname", gdgDetails.gdgName)
                        putString("cityname", args.chapter.city_name)
                        putString("countryname", args.chapter.country)
                        Log.d("store", "stored - ${gdgDetails.gdgName}")
                        putString("aboutgdg", gdgDetails.about)
                        putString("member", gdgDetails.membersNumber)

                        val stringorganizer = gson.toJson(organizerList)
                        val stringpastevent = gson.toJson(pastEventsList)
                        val stringupcoming = gson.toJson(upcomingEventlist)


                        putString("organzierslist", stringorganizer)
                        putString("pasteventslist", stringpastevent)
                        putString("upcomingeventlist", stringupcoming)
                        apply()
                    }
                }

                Log.d("upcomingeventinactivechapter", gdgDetails.upcomingEventsList.size.toString())
                if (upcomingEventlist.size != 0) {

                    upcoEventsAdapterupcoming.refreshData(upcomingEventlist)
                    upcoEventsAdapterupcoming.setOnItemClickListener(object :
                        UpcoEventsAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val uri = Uri.parse(upcomingEventlist[position].link)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }

                    })
                    upcomingEventsRecycler.visibility = View.VISIBLE
                    noUpcomingEventTextView.visibility = View.GONE
                } else {
                    noUpcomingEventTextView.visibility = View.VISIBLE
                    upcomingEventsRecycler.visibility = View.GONE
                }

                for (i in upcomingEventlist) {
                    Log.d("hello", i.toString())
                }
                eventsAdapterpast.refreshData(pastEventsList)

                organizerAdapter.refreshData(organizerList)
                if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
                if (scrollView.visibility == View.GONE) {
                    scrollView.visibility = View.VISIBLE
                }

                    gdgName.visibility = View.VISIBLE
                    gdgName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    cityName.visibility = View.VISIBLE
                    cityName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    countryName.visibility = View.VISIBLE
                    countryName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    member.visibility = View.VISIBLE
                    member.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    pasteventCardView.visibility = View.VISIBLE
                    pasteventCardView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    upcomingcardView.visibility = View.VISIBLE
                    upcomingcardView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    aboutgdgcardView.visibility = View.VISIBLE
                    aboutgdgcardView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    organizercardView.visibility = View.VISIBLE
                    organizercardView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        ),
                    )




            }
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