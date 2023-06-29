package com.example.googledeveloperscommunityvisualisationtool.fragments.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googledeveloperscommunityvisualisationtool.dataClass.gdgGroupClasses.GdgDataClass
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgChapModelFactory
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgScrapingRespo
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgViewModel
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentHomeBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapViewModelFact
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChaptersUrlEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomfactory
import com.google.android.material.search.SearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class Home : Fragment() {
    lateinit var progressBar: ProgressBar
    private lateinit var adapterlist: List<ChapterEntity>
    private lateinit var newadapterlist:MutableList<ChapterEntity>
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var gdgChaptersViewModel: GdgViewModel
    private lateinit var gdgChaptersList: ArrayList<GdgDataClass>
    private lateinit var chapUrlroomViewModel: ChapUrlroomViewModel
    private lateinit var chapterDatabaseViewModel:ChapterViewModel
    private lateinit var adapter: GdgChaptersAdapter
    private lateinit var searchView:SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root


        progressBar=binding.progressBar

        newadapterlist = mutableListOf()
        adapter = GdgChaptersAdapter(newadapterlist)
        binding.recyclerViewChapters.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewChapters.adapter = adapter


        searchView=binding.searchChaptersView






        sharedPref = activity?.getSharedPreferences("didShowPrompt", Context.MODE_PRIVATE)!!
        val prefEdit = sharedPref?.edit()

//        secondCardViewTapTarget()

        return view


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Chapter URL Database ViewModel
        chapUrlroomViewModel = ViewModelProvider(requireActivity(), ChapUrlroomfactory(requireContext())).get(ChapUrlroomViewModel::class.java)

        //For Chapter Scraping
        val gdgChapterRepo = GdgScrapingRespo()
        gdgChaptersViewModel = ViewModelProvider(requireActivity(), GdgChapModelFactory(gdgChapterRepo, requireContext())).get(GdgViewModel::class.java)


        //Chapter All details ViewModel
        chapterDatabaseViewModel=ViewModelProvider(requireActivity(),ChapViewModelFact(requireContext())).get(ChapterViewModel::class.java)
        Log.d("coroutines", "before checkurldatabase")

        checkurlDatabase()


    }

    override fun onDestroy() {
        super.onDestroy()
        chapUrlroomViewModel.readAllChapterUrlViewModel.removeObserver{}
        chapterDatabaseViewModel.readAllChaptersViewModel.removeObserver{}
    }


    //Check if chapter_url database is empty then fetch data and if
    private fun checkurlDatabase() {
        var flag1=0
        var urlListsize=0
         chapterDatabaseViewModel.readAllChaptersViewModel.observe(viewLifecycleOwner,Observer{chapterList->
             Log.d("coroutines","inside the checkurldatabase")
             if(flag1==0){
                 chapUrlroomViewModel.readAllChapterUrlViewModel.observe(viewLifecycleOwner,Observer { urlList->
                     Log.d("coroutines","inside the chapterUrlDatabase->${urlList.size}")
                     urlListsize=urlList.size
                     if(flag1==0){
                         flag1++
                         CoroutineScope(Dispatchers.IO).launch {
                             val job = CoroutineScope(Dispatchers.IO).launch {
                                 networkCheck()
                             }
                             job.join()
                             Log.d("coroutines","inside the chapterUrlDatabase->${urlList.size}")
                             delay(5000)
                             if (urlListsize == chapterList.size) {
                                 Log.d("coroutines","urlList.size == chapterList.size ${urlList.size} ${chapterList.size}")
                                 getChapterFromDatabase()
                             } else {
                                 Log.d("coroutines","urlList.size != chapterList.size ${urlList.size} ${chapterList.size}")
                                 getAllGdgChapter(chapterList.size)
                             }
                         }
                     }
                 })
             }
             getChapterFromDatabase()
         })
    }


    private fun getAllGdgChapter(j:Int) {
        Log.d("coroutines", "beforedelay")
        val job4=CoroutineScope(Dispatchers.Main).launch {
           chapUrlroomViewModel.readAllChapterUrlViewModel.observe(requireActivity(),
               Observer{ gdgURlChapterEntity ->
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
        CoroutineScope(Dispatchers.Main).launch{
            chapterDatabaseViewModel.readAllChaptersViewModel.observe(requireActivity(),Observer {it->
                adapterlist=it
                adapter.refreshData(adapterlist)
                if (progressBar.visibility==View.VISIBLE)progressBar.visibility=View.GONE
                newadapterlist=adapterlist.toMutableList()
                searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newadapterlist.clear()
                        val searchtext=newText!!.toLowerCase(Locale.getDefault())
                        if(searchtext.isNotEmpty()){
                            adapterlist.forEach {
                                if(it.gdgName.lowercase(Locale.getDefault()).contains(searchtext)){
                                    newadapterlist.add(it)
                                }
                            }
                            adapter.refreshData(newadapterlist)
                        }else{
                            newadapterlist.clear()
                            newadapterlist.addAll(adapterlist)
                            adapter.refreshData(newadapterlist)
                        }
                        return false
                    }
                })

                adapter.setOnItemClickListener(object :GdgChaptersAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val action= HomeDirections.actionHomeToGdgChapterDetails(newadapterlist[position])
                        findNavController().navigate(action)
                        onPause()
                    }
                })
            })
        }
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
                chapUrlroomViewModel.addChapterUrlViewModel(chapter)
            }
        }
        Log.d("coroutines","Inside the Job1 ended")
        job1.join()
        job1.cancel()

        Log.d("coroutines","job1 is ${job1.isActive} || ${job1.isCancelled}||${job1.isCompleted}")
        Log.d("coroutines","job1 is ${job1.isActive} || ${job1.isCancelled}||${job1.isCompleted}")
        Log.d("coroutines"," after the Job1 join")
    }

    private fun convertGdgDataTypes(gdgChaptersList: ArrayList<GdgDataClass>):ArrayList<ChaptersUrlEntity> {
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





}