package com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.Repository.UpcomingEventRepository
import com.example.googledeveloperscommunityvisualisationtool.ViewModel.UpcomingEventViewModel
import com.example.googledeveloperscommunityvisualisationtool.ViewModel.UpcomingEventViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentUpcomingEventsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpcomingEvents : Fragment() {
    lateinit var binding:FragmentUpcomingEventsBinding
    lateinit var upcomingEventViewModel:UpcomingEventViewModel
    lateinit var eventList:List<Result>
    lateinit var toggle:ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding=FragmentUpcomingEventsBinding.inflate(layoutInflater,container,false)
            val view= binding.root
            val upcomingEventRepository= UpcomingEventRepository(view.context)

            upcomingEventViewModel= ViewModelProvider(this, UpcomingEventViewModelFactory(upcomingEventRepository)).get(
                    UpcomingEventViewModel::class.java)

            CoroutineScope(Dispatchers.IO).launch{
                 eventList=upcomingEventViewModel.getResponseViewModel()
            }
            binding.recyclerView.layoutManager=LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
            binding.recyclerView.adapter=Adapter(eventList)


            return view
    }

}