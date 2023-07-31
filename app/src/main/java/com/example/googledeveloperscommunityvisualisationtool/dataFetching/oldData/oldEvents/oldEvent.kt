package com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents

import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.car.ui.recyclerview.CarUiListItemAdapterAdapterV1.ViewHolderWrapper
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Event
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentOldEventBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class oldEvent : Fragment() {
    val endpoint:oldEventArgs by navArgs()
    lateinit var binding: FragmentOldEventBinding
    lateinit var oldEventViewModel: OldEventViewModel
    lateinit var pasteventRecyclerView:RecyclerView
    lateinit var organizerrecyclerView:RecyclerView
    lateinit var gdgName:TextView
    lateinit var cityName:TextView
//    lateinit var countryname:TextView
    lateinit var eventList:List<Event>
    lateinit var organizerList: List<Organizer>
    lateinit var EventAdapter:oldEventAdapter
    lateinit var organizerAdapter:oldGdgOrganAdap
//    lateinit var progressBar: ProgressBar
    lateinit var scrollView:ScrollView
    lateinit var loadingAnimation:LottieAnimationView
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
//        countryname=binding.countryname
//        countryname.text=endpoint.chaptername.country

        eventList= listOf()
        pasteventRecyclerView=binding.eventRecyclerView
        pasteventRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        EventAdapter= oldEventAdapter(eventList)
        pasteventRecyclerView.adapter=EventAdapter


        organizerList= listOf()
        organizerrecyclerView=binding.OrganizersRecyclerView
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
//                if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                if(loadingAnimation.visibility==View.VISIBLE)loadingAnimation.visibility=View.GONE
                if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE
                pasteventRecyclerView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                organizerrecyclerView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                gdgName.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                cityName.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
//                countryname.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                organizerAdapter.refreshdata(organizerList)
            }
        }

    }
    override fun onResume() {
        super.onResume()
        val customAppBar = (activity as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton
        val backButton = customAppBar.backarrow

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton?.visibility = View.VISIBLE
            backButton?.visibility = View.GONE
        } else {
            menuButton?.visibility = View.GONE
            backButton?.visibility = View.VISIBLE
        }

        backButton?.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

    }


}