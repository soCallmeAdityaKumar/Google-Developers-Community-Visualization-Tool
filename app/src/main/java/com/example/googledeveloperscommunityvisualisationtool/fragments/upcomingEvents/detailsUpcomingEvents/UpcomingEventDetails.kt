package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldGdgOrganAdap
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventDetailsBinding
import com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.OrganizersAdapter
import com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.events
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    lateinit var progressBar: ProgressBar
    lateinit var scrollView:ScrollView
    lateinit var rsvp:TextView
    lateinit var organizerList:List<Organizers>
    lateinit var memberrecyclerView:RecyclerView
    lateinit var organizersAdapter: OrganizersAdapter
    lateinit var aboutcardView:CardView
    lateinit var membersCardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUpcomingEventDetailsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        progressBar=binding.progressBar
        scrollView=binding.scrollview

        progressBar.visibility=View.VISIBLE
        scrollView.visibility=View.GONE


        eventTitle=binding.eventsName
        address=binding.address
        gdgName=binding.gdgdName
        dateAndTime=binding.dateandtime
        desc=binding.desc
        rsvp=binding.rsvp
        memberrecyclerView=binding.memberRecyclerView
        aboutcardView=binding.aboutcardView
        membersCardView=binding.membersCardView





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
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getResponseModel(url.upcomingeventsUrl,requireContext())
                Log.d("eventdetails","after getResponse")
            }
            Log.d("eventdetails","before fetching")
            delay(5000)
            withContext(Dispatchers.Main){
                if(scrollView.visibility==View.GONE)scrollView.visibility=View.VISIBLE
                if(progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                val eventData= viewModel.returnEvents()
                gdgName.text=eventData.gdgName
                gdgName.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                eventTitle.text=eventData.title
                eventTitle.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                if(eventData.address.isEmpty()){
                    address.visibility=View.GONE
                }else{
                    address.text=eventData.address
                    address.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                }
                if(eventData.dateAndTime.isEmpty()){
                    dateAndTime.visibility=View.GONE
                }else{
                    dateAndTime.text=eventData.dateAndTime
                    dateAndTime.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                }
                desc.text=eventData.desc
                aboutcardView.visibility=View.VISIBLE
                aboutcardView.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))

                if(eventData.rsvp.isEmpty()){
                    rsvp.visibility=View.GONE
                }else{
                    rsvp.text=eventData.rsvp
                    rsvp.startAnimation(AnimationUtils.loadAnimation(requireContext(),android.R.anim.slide_in_left))
                }
                organizerList=eventData.mentors.toList()

                organizersAdapter.refreshData(organizerList)
            }

        }


    }

}