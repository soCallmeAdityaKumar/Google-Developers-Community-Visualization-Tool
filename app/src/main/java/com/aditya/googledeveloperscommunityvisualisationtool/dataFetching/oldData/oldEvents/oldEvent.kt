package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.HorizontalScrollView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.car.ui.recyclerview.CarUiListItemAdapterAdapterV1.ViewHolderWrapper
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.connection.LGConnectionTest
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Event
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentOldEventBinding
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventArgs
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.TourGDGThread
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.tourGDGDataclass
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.PastEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

class oldEvent : Fragment() {
    val endpoint: oldEventArgs by navArgs()
    lateinit var binding: FragmentOldEventBinding
    lateinit var oldEventViewModel: OldEventViewModel
    lateinit var pasteventRecyclerView:RecyclerView
    lateinit var organizerrecyclerView:RecyclerView
    lateinit var gdgName:TextView
    lateinit var cityName:TextView
//    lateinit var countryname:TextView
    lateinit var eventList:List<Event>
    lateinit var organizerList: List<Organizer>
    lateinit var tourGDG:TourGDGThread
    lateinit var EventAdapter:oldEventAdapter
    lateinit var organizerAdapter:oldGdgOrganAdap
    lateinit var NopastEventsText:TextView
    lateinit var noOrganizersText:TextView
//    lateinit var progressBar: ProgressBar
    lateinit var scrollView:ScrollView
    lateinit var loadingAnimation:LottieAnimationView
    var handler=Handler()
    lateinit var activ:FragmentActivity

    override fun onAttach(context: Context) {
        if (!requireActivity().isFinishing && !requireActivity().isDestroyed) {
            activ = requireActivity()
        }
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOldEventBinding.inflate(inflater,container,false)
        val view=binding.root

//        progressBar=binding.progressBar
        scrollView=binding.scrollView
        loadingAnimation=binding.loadinLottieAnimation

//        progressBar.visibility=View.VISIBLE
        loadingAnimation.playAnimation()
        scrollView.visibility=View.GONE

        gdgName=binding.gdgName
        gdgName.text=endpoint.chaptername.name
        cityName=binding.cityname
        cityName.text=endpoint.chaptername.city+ ","+endpoint.chaptername.country
        noOrganizersText=binding.NoOrganizersText
        NopastEventsText=binding.noPastEventText
//        countryname=binding.countryname
//        countryname.text=endpoint.chaptername.country

        NopastEventsText.visibility=View.VISIBLE
        eventList= listOf()
        pasteventRecyclerView=binding.eventRecyclerView
        pasteventRecyclerView.visibility=View.GONE
        pasteventRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        EventAdapter= oldEventAdapter(eventList)
        pasteventRecyclerView.adapter=EventAdapter

        noOrganizersText.visibility=View.VISIBLE
        organizerList= listOf()
        organizerrecyclerView=binding.OrganizersRecyclerView
        organizerrecyclerView.visibility=View.GONE
        organizerrecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        organizerAdapter= oldGdgOrganAdap(organizerList)
        organizerrecyclerView.adapter=organizerAdapter

        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val oldEventRepository=oldEventRepository(requireContext())
        oldEventViewModel=ViewModelProvider(this,oldEventViewfactory(oldEventRepository,requireContext())).get(OldEventViewModel::class.java)

        val job=CoroutineScope(Dispatchers.IO).launch {
            oldEventViewModel.getResponse(endpoint.chaptername.urlname)
            Log.d("oldevent","reponse sent")
        }
        viewLifecycleOwner.lifecycleScope.launch {
//        CoroutineScope(Dispatchers.IO).launch {
            job.join()
            delay(4000)
            Log.d("oldevent","after response")
            withContext(Dispatchers.Main){
                var gdgList=oldEventViewModel.getOldEventsViewModel()
                eventList=gdgList.events
                Log.d("oldevent",eventList.size.toString())
                organizerList=gdgList.organizers
                Log.d("oldevent",organizerList.size.toString())
//                if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                if(loadingAnimation.visibility==View.VISIBLE)loadingAnimation.visibility=View.GONE
                if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE
                pasteventRecyclerView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                organizerrecyclerView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                gdgName.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                cityName.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
//                countryname.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                if(eventList.isNotEmpty()){
                    EventAdapter.refreshdata(eventList)
                    pasteventRecyclerView.visibility=View.VISIBLE
                    NopastEventsText.visibility=View.GONE
                }
                if(organizerList.isNotEmpty()){
                    organizerAdapter.refreshdata(organizerList)
                    organizerrecyclerView.visibility=View.VISIBLE
                    noOrganizersText.visibility=View.GONE
                }
            }
        }

    }
    override fun onResume() {
        super.onResume()
        val customAppBar = (activ as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton.setBackgroundResource(R.drawable.baseline_menu_24)

//            menuButton?.visibility = View.VISIBLE
//            backButton?.visibility = View.GONE
        } else {
            menuButton.setBackgroundResource(R.drawable.backarrow)
//            menuButton?.visibility = View.GONE
//            backButton?.visibility = View.VISIBLE
            menuButton?.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }
        }

        handler.postDelayed({
            tour()
        },5000)
    }


    private fun tour() {
        val isConnected = AtomicBoolean(false)
        LGConnectionTest.testPriorConnection(activ, isConnected)
        val sharedPreferences = activ.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name,
            Context.MODE_PRIVATE
        )
        val tourOrganizer=convertOrganizerToOrganizers(organizerList)
        val tourPastEvent=convertEventToPastEvent(eventList)
        handler.postDelayed({
            if (isConnected.get()) {
                try {
                    val lgConnectionManager = LGConnectionManager.getInstance()
                    lgConnectionManager!!.startConnection()
                    val lgCommand = LGCommand(
                        ActionBuildCommandUtility.buildCommandCleanSlaves(),
                        LGCommand.CRITICAL_MESSAGE, object : LGCommand.Listener {
                            override fun onResponse(response: String?) {

                            }
                        })
                    lgConnectionManager.addCommandToLG(lgCommand)
                }catch (e: Exception){
                    println("Could not connect to LG")
                }
//                showDialog(requireActivity(), "Starting the GDG TOUR")
                tourGDG = TourGDGThread(
                    tourGDGDataclass(
                        "",
                        endpoint.chaptername.name,
                        endpoint.chaptername.status,
                        endpoint.chaptername.lat,
                        endpoint.chaptername.lon,
                        endpoint.chaptername.city,
                        endpoint.chaptername.country,
                        tourOrganizer,
                        tourPastEvent,
                        listOf()
                    ) ,
                    activ,
                )
                tourGDG!!.start()
            }
            loadConnectionStatus(sharedPreferences)
        }, 5000)
    }

    private fun loadConnectionStatus(sharedPreferences: SharedPreferences) {
        val isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        val act=activ as MainActivity
        if (isConnected) {
            act.binding.appBarMain.LGConnected.visibility=View.VISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.INVISIBLE
        } else {
            act.binding.appBarMain.LGConnected.visibility=View.INVISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.VISIBLE
        }
    }

    fun convertOrganizerToOrganizers(organizer: List<Organizer>):List<Organizers>{
        var mutableList= mutableListOf<Organizers>()
        for(org in organizer){
            val s=Organizers(org.name,"","",org.photo.photo_link)
            mutableList.add(s)
        }
        return mutableList.toList()
    }
    fun convertEventToPastEvent(event:List<Event>):List<PastEvents>{
        val mutableList= mutableListOf<PastEvents>()
        for(e in event){
            val k=PastEvents(e.name,e.local_date,e.description,e.link)
            mutableList.add(k)
        }
        return mutableList.toList()
    }

    override fun onDestroyView() {
        handler.removeCallbacks {  }
        viewLifecycleOwner.lifecycleScope.cancel()
        super.onDestroyView()
    }
}