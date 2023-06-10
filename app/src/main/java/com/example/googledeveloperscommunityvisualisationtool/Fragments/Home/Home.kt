package com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.GdgGroupDataClassItem
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgChaptersViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgScrapingRespository
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgViewModel
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentHomeBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.GdgChapterDatabaseViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.GdgChaptersEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.GdgDatabaseViewModelfactory
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Home : Fragment() {
//    lateinit var adapter: GdgChaptersAdapter
    private lateinit var binding:FragmentHomeBinding
    private lateinit var sharedPref:SharedPreferences
    private  lateinit var gdgChaptersViewModel:GdgViewModel
    private lateinit var gdgChaptersList:ArrayList<GdgGroupDataClassItem>
    private  lateinit var gdgChapterDatabaseViewModel: GdgChapterDatabaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater,container, false)
        val view=binding.root

         sharedPref= activity?.getSharedPreferences("didShowPrompt",Context.MODE_PRIVATE)!!
        val prefEdit=sharedPref?.edit()
        secondCardViewTapTarget()

        return view


    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gdgChapterRepo=GdgScrapingRespository()
        //For Chapter Database
        gdgChapterDatabaseViewModel=ViewModelProvider(this,GdgDatabaseViewModelfactory(requireContext())).get(GdgChapterDatabaseViewModel::class.java)

        //For Chapter Scraping
        gdgChaptersViewModel=ViewModelProvider(this,GdgChaptersViewModelFactory(gdgChapterRepo,requireContext())).get(GdgViewModel::class.java)
        networkCheck()


    }

    private fun networkCheck() {
        if(gdgChaptersViewModel.isNetworkAvailable()){
            getAllGdgChapters()
        }else{
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllGdgChapters() {
        CoroutineScope(Dispatchers.IO).launch {
            gdgChaptersList=gdgChaptersViewModel.getChaptersViewModel()
            val gdgChapterEntityList=convertGdgDataTypes(gdgChaptersList)
            for( chapter in gdgChapterEntityList){
                gdgChapterDatabaseViewModel.addChapterViewModel(chapter)
            }
            Log.d("gdgChapterList",gdgChaptersList.size.toString())
        }
    }

    private fun convertGdgDataTypes(gdgChaptersList: ArrayList<GdgGroupDataClassItem>):ArrayList<GdgChaptersEntity> {
        val gdgChaptersEntityList= ArrayList<GdgChaptersEntity>()
        for(chapter in gdgChaptersList){
            val gdgchapter=GdgChaptersEntity(
                0,
                chapter.avatar,
                chapter.banner,
                chapter.city,
                chapter.city_name,
                chapter.country,
                chapter.latitude ,
                chapter.longitude ,
                chapter.url)
            gdgChaptersEntityList.add(gdgchapter)
        }
        return gdgChaptersEntityList
    }


    private fun secondCardViewTapTarget() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.requireActivity(),
                TapTarget.forView(
                    binding.secondcardView,
                    "Top 10 Events",
                    ""
                )
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(120),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
                        showThirdTaptargetTapView()
                    }
                })
        }
    }

    private fun showThirdTaptargetTapView() {
        if(!sharedPref.getBoolean("didShowPrompt",false)){
            TapTargetView.showFor(this.activity,
                TapTarget.forView(
                    binding.Top10EventsRecyclerView,
                    "Top 10 Events ",
                    "This shows the Top 10 events according to the Number of the RSVP in events"
                )
                    .outerCircleColor(androidx.appcompat.R.color.material_deep_teal_200)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(10)
                    .descriptionTextColor(R.color.black)
                    .textColor(R.color.black)
                    .textTypeface(Typeface.SANS_SERIF)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(false)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView?) {
                        super.onTargetClick(view)
//                        val prefEditor = sharedPref.edit()
//                        prefEditor.putBoolean("didShowPrompt", true)
//                        prefEditor.apply()
                    }
                })
        }
    }



}