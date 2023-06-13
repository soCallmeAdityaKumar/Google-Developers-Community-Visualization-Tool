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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.GdgGroupDataClassItem
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgChaptersViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgScrapingRespository
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgViewModel
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.GdgChaptersAdapter
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentHomeBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.ChapterUrlDatabaseViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.ChaptersUrlEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.ChapterUrlDatabaseViewModelfactory
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {
    //    lateinit var adapter: GdgChaptersAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapterlist: List<ChapterEntity>
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var gdgChaptersViewModel: GdgViewModel
    private lateinit var gdgChaptersList: ArrayList<GdgGroupDataClassItem>
    private lateinit var chapterUrlDatabaseViewModel: ChapterUrlDatabaseViewModel
    private lateinit var chapterDatabaseViewModel:ChapterViewModel
    private lateinit var adapter: GdgChaptersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        adapterlist = listOf()
        adapter = GdgChaptersAdapter(adapterlist)
        binding.recyclerViewChapters.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewChapters.adapter = adapter
        sharedPref = activity?.getSharedPreferences("didShowPrompt", Context.MODE_PRIVATE)!!
        val prefEdit = sharedPref?.edit()
        secondCardViewTapTarget()

        return view


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gdgChapterRepo = GdgScrapingRespository()
        //For Chapter Database
        chapterUrlDatabaseViewModel =
            ViewModelProvider(this, ChapterUrlDatabaseViewModelfactory(requireContext())).get(
                ChapterUrlDatabaseViewModel::class.java
            )

        //For HomeViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //For Chapter Scraping
        gdgChaptersViewModel = ViewModelProvider(
            this,
            GdgChaptersViewModelFactory(gdgChapterRepo, requireContext())
        ).get(GdgViewModel::class.java)


        //Chapter ViewModel
        chapterDatabaseViewModel=ViewModelProvider(this,ChapterViewModelFactory(requireContext())).get(ChapterViewModel::class.java)

        checkurlDatabase()
        Log.d("gdglistsize", adapterlist.size.toString())


    }

    private fun checkurlDatabase() {
        chapterUrlDatabaseViewModel.readAllChapterUrlViewModel.observe(viewLifecycleOwner, Observer {list->
            if(list.isEmpty()){
                networkCheck()

            }else{

                chapterDatabaseViewModel.readAllChaptersViewModel.observe(viewLifecycleOwner,
                    Observer { chapterEntity ->
                            if (chapterEntity.isEmpty()) {
                                getAllGdgChapter()
                            } else {
                                getChapterFromDatabase()
                            }


                    })

            }
        })
    }

    private fun getChapterFromDatabase() {
        chapterDatabaseViewModel.readAllChaptersViewModel.observe(viewLifecycleOwner, Observer {it->
            adapterlist=it
            adapter.refreshData(adapterlist)
            binding.recyclerViewChapters.adapter = adapter

            adapter.setOnItemClickListener(object :GdgChaptersAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {

                }

            })
        })
    }

    private fun networkCheck() {

        if (gdgChaptersViewModel.isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                async { storeUrlinDatabase()}.await()
//                delay(4000)
//                getAllGdgChapter()
            }

        } else {
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllGdgChapter() {
        Log.d("getAllgdgChapters", "beforedelay")
        CoroutineScope(Dispatchers.Main).launch {
            chapterUrlDatabaseViewModel.readAllChapterUrlViewModel.observe(viewLifecycleOwner,
                Observer { gdgChapterEntity ->
                    for (chapter in gdgChapterEntity) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val details = homeViewModel.getResponse(chapter.url)
                            withContext(Dispatchers.Main) {
                                chapterDatabaseViewModel.addChaptersViewModel(
                                    ChapterEntity(
                                        chapter.avatar,
                                        chapter.banner,
                                        chapter.city,
                                        chapter.city_name,
                                        chapter.country,
                                        chapter.latitude,
                                        chapter.longitude,
                                        chapter.url,
                                        details.gdgName,
                                        details.membersNumber,
                                        details.about
                                    )
                                )
                            }
                        }
                    }
                })
        }

    }




    private fun storeUrlinDatabase() {
        Log.d("storeindatabase","beforedelay")
        CoroutineScope(Dispatchers.IO).launch {
            gdgChaptersList = gdgChaptersViewModel.getChaptersViewModel()
            val gdgChapterEntityList = convertGdgDataTypes(gdgChaptersList)
                for (chapter in gdgChapterEntityList) {
                    chapterUrlDatabaseViewModel.addChapterUrlViewModel(chapter)
                }
            Log.d("storeindatabase","afterdelay")
        }

    }

    private fun convertGdgDataTypes(gdgChaptersList: ArrayList<GdgGroupDataClassItem>):ArrayList<ChaptersUrlEntity> {
        val chaptersUrlEntityList= ArrayList<ChaptersUrlEntity>()
        for(chapter in gdgChaptersList){
            val gdgchapter=ChaptersUrlEntity(
                chapter.avatar,
            chapter.banner,
            chapter.city_name,
            chapter.city,
            chapter.country,
            chapter.latitude,
            chapter.longitude,
            chapter.url)
            chaptersUrlEntityList.add(gdgchapter)
        }
        return chaptersUrlEntityList
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