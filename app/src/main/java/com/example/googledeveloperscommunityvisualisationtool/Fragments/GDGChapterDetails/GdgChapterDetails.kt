package com.example.googledeveloperscommunityvisualisationtool.Fragments.GDGChapterDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.DataBinderMapperImpl
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgChaptersViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgScrapingRespository
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgViewModel
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentGdgChapterDetailsBinding
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
    lateinit var binding:FragmentGdgChapterDetailsBinding
    lateinit var gdgViewModel:GdgViewModel


     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=FragmentGdgChapterDetailsBinding.inflate(layoutInflater,container, false)
         val view=binding.root

         gdgName=binding.gdgname
         cityName=binding.cityname
         countryName=binding.countryname
         aboutGdg=binding.aboutgdg
         organizerRecyclerView=binding.orgainzersrecycler
         upcomingEventsRecycler=binding.upcomingrecyclerview
         pasteventsRecycler=binding.pasteventsrecycler



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gdgChapterRepo=GdgScrapingRespository()
        gdgViewModel = ViewModelProvider(this, GdgChaptersViewModelFactory(gdgChapterRepo, requireContext())).get(GdgViewModel::class.java)

        getDetails()
    }

    private fun getDetails() {

    }

}