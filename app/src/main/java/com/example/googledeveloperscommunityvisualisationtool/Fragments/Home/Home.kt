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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.GdgGroupDataClassItem
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgChaptersViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgScrapingRespository
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgViewModel
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents.url
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {
    lateinit var progressBar: ProgressBar
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

        progressBar=binding.progressBar


        adapterlist = listOf()
        adapter = GdgChaptersAdapter(adapterlist)
        binding.recyclerViewChapters.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewChapters.adapter = adapter


        sharedPref = activity?.getSharedPreferences("didShowPrompt", Context.MODE_PRIVATE)!!
        val prefEdit = sharedPref?.edit()


//        secondCardViewTapTarget()

        return view


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Chapter URL Database ViewModel
        chapterUrlDatabaseViewModel = ViewModelProvider(this, ChapterUrlDatabaseViewModelfactory(requireContext())).get(ChapterUrlDatabaseViewModel::class.java)

        //For Chapter Scraping
        val gdgChapterRepo = GdgScrapingRespository()
        gdgChaptersViewModel = ViewModelProvider(this, GdgChaptersViewModelFactory(gdgChapterRepo, requireContext())).get(GdgViewModel::class.java)


        //Chapter All details ViewModel
        chapterDatabaseViewModel=ViewModelProvider(this,ChapterViewModelFactory(requireContext())).get(ChapterViewModel::class.java)
        Log.d("coroutines", "before checkurldatabase")

        checkurlDatabase()
        Log.d("gdglistsize", adapterlist.size.toString())


    }


    //Check if chapter_url database is empty then fetch data and if
    private fun checkurlDatabase() {
        var flag=0
        Log.d("coroutines", "inside checkurldatabase")
        if(progressBar.visibility==View.GONE)progressBar.visibility=View.VISIBLE
        chapterDatabaseViewModel.readAllChaptersViewModel.observe(viewLifecycleOwner, Observer {it->
            if(it.size in 0 until 969&&flag==0){
                //if chapter details database is empty
                flag++
                Log.d("url size",it.size.toString())
                chapterUrlDatabaseViewModel.readAllChapterUrlViewModel.observe(viewLifecycleOwner,
                    Observer {urllist->
                        if(urllist.isEmpty()){
                             CoroutineScope(Dispatchers.IO).launch {
                                networkCheck()
                                 Log.d("coroutines","after network check")
                             }
                        } else if(urllist.size==969&&it.size!=urllist.size){
                            Log.d("urlsize","after database ${urllist.size}")
                            CoroutineScope(Dispatchers.IO).launch {
//                                delay(3000)
                                Log.d("coroutines","getallgdgchapter started")
                                getAllGdgChapter(it.size)
                            }
                        }
                    })
            }else if(it.isNotEmpty()){
                    Log.d("coroutines","before getchapterdatabase ")
                    getChapterFromDatabase()

            }
        })
    }


    private fun getAllGdgChapter(j:Int) {
        Log.d("coroutines", "beforedelay")
        val job4=CoroutineScope(Dispatchers.Main).launch {
           chapterUrlDatabaseViewModel.readAllChapterUrlViewModel.observe(viewLifecycleOwner,
                Observer { gdgURlChapterEntity ->
                    Log.d("home","starting from ${gdgURlChapterEntity[j].url}")
                    CoroutineScope(Dispatchers.IO).launch {
                        for (i in j until gdgURlChapterEntity.size) {
                            gdgChaptersViewModel.getCompleteGDGdetails(gdgURlChapterEntity[i].url)
                            val details=gdgChaptersViewModel.getdetails()
                            Log.d("gdgdetails","gdgdetials response got")
                            chapterDatabaseViewModel.addChaptersViewModel(
                                ChapterEntity(
                                    gdgURlChapterEntity[i].avatar,
                                    gdgURlChapterEntity[i].banner,
                                    gdgURlChapterEntity[i].city,
                                    gdgURlChapterEntity[i].city_name,
                                    gdgURlChapterEntity[i].country,
                                    gdgURlChapterEntity[i].latitude,
                                    gdgURlChapterEntity[i].longitude,
                                    gdgURlChapterEntity[i].url,
                                    details.gdgName,
                                    details.membersNumber,
                                    details.about
                                )
                            )
                            Log.d("gdgddetails","gdgdetials response added to database")
                        }
                    }
                })
            getChapterFromDatabase()

        }
//        CoroutineScope(Dispatchers.Main).launch {
//            Log.d("coroutines","job4 is ${job4.isActive}||${job4.isCompleted}||${job4.isCancelled}")
//            delay(3000)
//            job4.cancelAndJoin()
//            Log.d("coroutines","job4 is ${job4.isActive}||${job4.isCompleted}||${job4.isCancelled}")
//            Log.d("coroutines","calling getChapterFrom Database")
//            getChapterFromDatabase()
//        }

    }

    private fun getChapterFromDatabase() {
        chapterDatabaseViewModel.readAllChaptersViewModel.observe(viewLifecycleOwner, Observer {it->
            adapterlist=it
            adapter.refreshData(adapterlist)
            if (progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE

            adapter.setOnItemClickListener(object :GdgChaptersAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    val action=HomeDirections.actionHomeToGdgChapterDetails(it[position])
                    findNavController().navigate(action)
                }
            })
        })
    }

    private suspend fun networkCheck() {
        Log.d("coroutines", "inside network check")
        if (gdgChaptersViewModel.isNetworkAvailable()) {
                storeUrlinDatabase()
        } else {
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }






    private suspend fun storeUrlinDatabase() {
        Log.d("coroutines","beforedelay")
        Log.d("coroutines", "inside store url database")
        val job1=CoroutineScope(Dispatchers.IO).launch {
            Log.d("coroutines","Inside the Job1 started")
            val job2=CoroutineScope(Dispatchers.IO).launch {
                Log.d("coroutines","Inside the Job2 started ${Thread.currentThread().name}")

                gdgChaptersViewModel.getChaptersViewModel()
                Log.d("coroutines","Inside the Job2 ended")
            }
            delay(3000)
            job2.join()
            Log.d("coroutines","After the Job2 join")
            gdgChaptersList=gdgChaptersViewModel.returnGDGChapterViewModel()
            val gdgChapterEntityList = convertGdgDataTypes(gdgChaptersList)
            Log.d("coroutines","before database ${gdgChapterEntityList.size}")
            for (chapter in gdgChapterEntityList) {
                chapterUrlDatabaseViewModel.addChapterUrlViewModel(chapter)
            }
        }
        Log.d("coroutines","Inside the Job1 ended")
        job1.join()
        job1.cancel()

        Log.d("coroutines","job1 is ${job1.isActive} || ${job1.isCancelled}||${job1.isCompleted}")
        Log.d("coroutines","job1 is ${job1.isActive} || ${job1.isCancelled}||${job1.isCompleted}")
        Log.d("coroutines"," after the Job1 join")
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