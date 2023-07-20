package com.example.googledeveloperscommunityvisualisationtool.fragments.home

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.R
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
import com.example.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.util.HashMap
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
    private lateinit var pieChartAdapter: PieChartAdapter
    lateinit var countryCount:MutableList<CountryCountData>
    private lateinit var pieRecyclerView:RecyclerView
    private lateinit var mostChaptersCountry:HashMap<String,Int>
    private lateinit var searchView:androidx.appcompat.widget.SearchView
    private lateinit var pieChart: PieChart
    private lateinit var statisticsText:TextView
    lateinit var gdgChapterRecyclerView: RecyclerView
    lateinit var scrollView:ScrollView
    lateinit var secondcardview:CardView
    lateinit var activeChapterText:TextView
    lateinit var thirdCardView:CardView
    lateinit var pieSharePref:SharedPreferences
    lateinit var pieEditor:SharedPreferences.Editor
    lateinit var cloudImageView:ImageView
    lateinit var statisticsImageView: ImageView
    private var fragmentLifecycleOwner: LifecycleOwner?=null
    private lateinit var themeSharedPreferences: SharedPreferences
    var sortedMap: HashMap<String,Int> = hashMapOf()

    var flag2=0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("gdgchapter","onAttach() called")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("home","onCreateView() called")
        fragmentLifecycleOwner=viewLifecycleOwner

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        flag2=0
        scrollView=binding.scrollView
        gdgChapterRecyclerView=binding.recyclerViewChapters
        searchView=binding.searchChaptersView
        pieRecyclerView=binding.pieChartRecyclerView
        secondcardview=binding.SecondcardView
        thirdCardView=binding.thirdCardview
        statisticsText=binding.firstcardviewTextView
        progressBar=binding.progressBar
        activeChapterText=binding.fourthcardviewTextView
        cloudImageView=binding.cloudImage
        statisticsImageView=binding.statisticsImage


        scrollView.visibility=View.GONE
        gdgChapterRecyclerView.visibility=View.GONE
        thirdCardView.visibility=View.GONE
        secondcardview.visibility=View.GONE
        statisticsText.visibility=View.GONE
        activeChapterText.visibility=View.GONE
        progressBar.visibility=View.VISIBLE


        pieChart=binding.PieChart
        mostChaptersCountry= hashMapOf()
        pieSharePref=activity?.getSharedPreferences("PieSharedPref",Context.MODE_PRIVATE)!!
        pieEditor=pieSharePref.edit()

        newadapterlist = mutableListOf()
        adapter = GdgChaptersAdapter(newadapterlist)
        gdgChapterRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        gdgChapterRecyclerView.adapter = adapter


        countryCount= mutableListOf()
        pieRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        pieChartAdapter=PieChartAdapter(countryCount)
        pieRecyclerView.adapter=pieChartAdapter


        themeSharedPreferences=activity?.getSharedPreferences("Theme",Context.MODE_PRIVATE)!!
        val night=themeSharedPreferences.getBoolean("Night",false)
        if(night){
            cloudImageView.setImageDrawable(resources.getDrawable(R.drawable.cloud_dark_logo))
            statisticsImageView.setImageDrawable(resources.getDrawable(R.drawable.statistics_dark_logo))
        }else{
            cloudImageView.setImageDrawable(resources.getDrawable(R.drawable.cloud_light_logo))
            statisticsImageView.setImageDrawable(resources.getDrawable(R.drawable.statistics_light_logo))
        }


        sharedPref = activity?.getSharedPreferences("didShowPrompt", Context.MODE_PRIVATE)!!
        val prefEdit = sharedPref?.edit()


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

        loadConnectionStatus()

        startPieChartmaking()

    }

    private fun startPieChartmaking() {
        var countryList:List<ChaptersUrlEntity> = listOf()
        if(pieSharePref.all.size<10){
            chapUrlroomViewModel.readAllChapterUrlViewModel.observe(fragmentLifecycleOwner!!, Observer {
                countryList=it
                CoroutineScope(Dispatchers.IO).launch {
                    if(countryList.isNotEmpty()) {
                        delay(5000)
                        for (i in 0 until countryList.size) {
                            increment(mostChaptersCountry, countryList[i].country)
                        }
                        withContext(Dispatchers.Main) {
                            setDataToPie()
                            if (progressBar.visibility == View.VISIBLE) progressBar.visibility =
                                View.GONE
                            statisticsText.visibility = View.VISIBLE
                            secondcardview.visibility = View.VISIBLE
                            statisticsText.startAnimation(
                                AnimationUtils.loadAnimation(
                                    requireContext(),
                                    android.R.anim.slide_in_left
                                )
                            )
                            secondcardview.startAnimation(
                                AnimationUtils.loadAnimation(
                                    requireContext(),
                                    android.R.anim.slide_in_left
                                )
                            )
                        }
                    }
                }
            })
        }
        else{
            setDataToPieFromPref()
        }


    }

    private fun setDataToPieFromPref() {
        var j=0
        val listOfColor= listOf("#FFA726","#66BB6A","#EF5350","#29B6F6","#FFC8DD","#2B2D42","#8D99AE","#D5BDAF","#E4C1F9","#7400B8")
        pieSharePref.all.forEach {map->
            pieChart.addPieSlice(PieModel(map.key,map.value as Float,Color.parseColor(listOfColor[j])))
            countryCount.add(CountryCountData(listOfColor[j],map.key,Integer.parseInt(map.value.toString().dropLast(2))))
            j++
        }
        pieChartAdapter.refreshData(countryCount)
        statisticsText.visibility = View.VISIBLE
        secondcardview.visibility = View.VISIBLE
        statisticsText.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                android.R.anim.slide_in_left
            )
        )
        secondcardview.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                android.R.anim.slide_in_left
            )
        )
        pieChart.startAnimation()
    }

    override fun onResume() {
        super.onResume()
        Log.d("home","onResume() called")
        checkurlDatabase()

    }

    private fun loadConnectionStatus() {
        val sharedPreferences = activity?.getSharedPreferences(
            ConstantPrefs.SHARED_PREFS.name,
            Context.MODE_PRIVATE
        )

        val isConnected = sharedPreferences?.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        val act=activity as MainActivity
        if (isConnected!!) {
            act.binding.appBarMain.connectionStatus.text="Connected"
            act.binding.appBarMain.connectionStatus.setTextColor(Color.parseColor("#52b788"))
        } else {
            act.binding.appBarMain.connectionStatus.text="Not Connected"
            act.binding.appBarMain.connectionStatus.setTextColor(Color.parseColor("#ba181b"))

        }
    }


    //Check if chapter_url database is empty then fetch data and if
    private fun checkurlDatabase() {
        var flag1=0
        var urlListsize=0
         chapterDatabaseViewModel.readAllChaptersViewModel.observe(fragmentLifecycleOwner!!,Observer{chapterList->
             Log.d("coroutines","inside the checkurldatabase")

             if(flag1==0){
                 chapUrlroomViewModel.readAllChapterUrlViewModel.observe(fragmentLifecycleOwner!!,Observer { urlList ->
                     Log.d("coroutines", "inside the chapterUrlDatabase->${urlList.size}")

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
           chapUrlroomViewModel.readAllChapterUrlViewModel.observe(fragmentLifecycleOwner!!,
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

    }

    private  fun setDataToPie() {

        var j=0
        val listOfColor= listOf("#FFA726","#66BB6A","#EF5350","#29B6F6","#FFC8DD","#2B2D42","#8D99AE","#D5BDAF","#E4C1F9","#7400B8")
        Log.d("size",mostChaptersCountry.size.toString())

        if(mostChaptersCountry.size>10){
            mostChaptersCountry.entries.sortedByDescending { it.value }.take(10).forEach{ sortedMap[it.key] = it.value}
            sortedMap.forEach{(k,v)->Log.d("sorted","$k->$v")}
            sortedMap.forEach { (k, v) ->
                pieEditor.apply{
                    pieEditor.putFloat(k, v.toFloat())
                    apply()
                }
                pieChart.addPieSlice(PieModel(k, v.toFloat(),Color.parseColor(listOfColor[j])))
                countryCount.add(CountryCountData(listOfColor[j],k,v))
                j++
            }
            pieChartAdapter.refreshData(countryCount)
            pieChart.startAnimation()
        }
    }

    fun <K> increment(map: HashMap<K, Int>, key: K) {

      when(val count=map[key]){
          null->map[key]=0
          else->map[key]=count+1
      }
    }

    private fun getChapterFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch{

            chapterDatabaseViewModel.readAllChaptersViewModel.observe(fragmentLifecycleOwner!!,Observer { it ->

                adapterlist = it
                adapter.refreshData(adapterlist)
                if(it.isNotEmpty()){
                if (flag2 == 0) {
                    flag2 = 1
                    if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE
                    activeChapterText.visibility = View.VISIBLE
                    gdgChapterRecyclerView.visibility = View.VISIBLE
                    gdgChapterRecyclerView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )

                }
                newadapterlist = adapterlist.toMutableList()
                //store or increase count in mostChaptersCountry

                searchView.setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newadapterlist.clear()
                        val searchtext = newText!!.toLowerCase(Locale.getDefault())
                        if (searchtext.isNotEmpty()) {
                            adapterlist.forEach {
                                if (it.gdgName.lowercase(Locale.getDefault())
                                        .contains(searchtext)
                                ) {
                                    newadapterlist.add(it)
                                }
                            }
                            adapter.refreshData(newadapterlist)
                        } else {
                            newadapterlist.clear()
                            newadapterlist.addAll(adapterlist)
                            adapter.refreshData(newadapterlist)
                        }
                        return false
                    }
                })



                adapter.setOnItemClickListener(object : GdgChaptersAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val action =
                            HomeDirections.actionHomeToGdgChapterDetails(newadapterlist[position])
                        findNavController().navigate(action)
                        onPause()
                    }
                })
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        chapterDatabaseViewModel.readAllChaptersViewModel.removeObserver{fragmentLifecycleOwner!!}
        chapUrlroomViewModel.readAllChapterUrlViewModel.removeObserver{fragmentLifecycleOwner!!}

    }

}