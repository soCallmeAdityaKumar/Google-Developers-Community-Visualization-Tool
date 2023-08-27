package com.aditya.googledeveloperscommunityvisualisationtool.fragments.home

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.createSavedStateHandle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.dataClass.gdgGroupClasses.GdgDataClass
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgChapModelFactory
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgScrapingRespo
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapViewModelFact
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChaptersUrlEntity
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomfactory
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentHomeBinding
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.HomeDirections
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.util.HashMap
import java.util.Locale
import javax.net.ssl.HandshakeCompletedListener
import kotlin.math.log
import kotlin.math.sign

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
    lateinit var countryRecyclerView:RecyclerView
    lateinit var countryAdapter:CounrtyAdapter
    lateinit var counrtyList:MutableList<String>
    private lateinit var pieRecyclerView:RecyclerView
    private lateinit var mostChaptersCountry:HashMap<String,Int>
    private lateinit var searchView:androidx.appcompat.widget.SearchView
    private lateinit var pieChart: PieChart
    private lateinit var statisticsText:TextView
    private lateinit var loadingAnimation:LottieAnimationView
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
    private lateinit var  objectsToAnimate: List<View>
    var forPieMaking=0
    var hanlder=Handler()
    lateinit var flagSharedpref:SharedPreferences
    lateinit var flagSharedEditor:SharedPreferences.Editor
    var flag2=0
    var flag1=0
    var flag3=0
    var flag4=0
    var urlGetChapterList= listOf<ChaptersUrlEntity>()


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
//        progressBar=binding.progressBar
        loadingAnimation=binding.loadinlottieanimation
        activeChapterText=binding.fourthcardviewTextView
        cloudImageView=binding.cloudImage
        statisticsImageView=binding.statisticsImage
        countryRecyclerView=binding.countryRecycler



        scrollView.visibility=View.GONE
        gdgChapterRecyclerView.visibility=View.GONE
        thirdCardView.visibility=View.GONE
        secondcardview.visibility=View.GONE
        statisticsText.visibility=View.GONE
        activeChapterText.visibility=View.GONE
//        progressBar.visibility=View.VISIBLE
        loadingAnimation.visibility=View.VISIBLE
        loadingAnimation.playAnimation()
        statisticsImageView.visibility=View.GONE


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

        counrtyList= mutableListOf()
        countryAdapter= CounrtyAdapter(counrtyList)
        countryRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        countryAdapter=CounrtyAdapter(counrtyList)
        countryRecyclerView.adapter=countryAdapter

        themeSharedPreferences=activity?.getSharedPreferences("Theme",Context.MODE_PRIVATE)!!

        flagSharedpref=activity?.getSharedPreferences("Flags",Context.MODE_PRIVATE)!!
        flagSharedEditor=flagSharedpref.edit()
        flag1=flagSharedpref.getInt("flag1",0)
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

//        objectsToAnimate= listOf(binding.cloudFlyingImage,binding.gdgImage)
//        startFlyingAnimation(binding.avatarImageView)


        return view


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Chapter URL Database ViewModel
        chapUrlroomViewModel = ViewModelProvider(requireActivity(), ChapUrlroomfactory(requireContext())).get(
            ChapUrlroomViewModel::class.java)

        //For Chapter Scraping
        val gdgChapterRepo = GdgScrapingRespo()
        gdgChaptersViewModel = ViewModelProvider(requireActivity(), GdgChapModelFactory(gdgChapterRepo, requireContext())).get(GdgViewModel::class.java)


        //Chapter All details ViewModel
        chapterDatabaseViewModel=ViewModelProvider(requireActivity(),ChapViewModelFact(requireContext())).get(ChapterViewModel::class.java)
        Log.d("coroutines", "before checkurldatabase")

        loadConnectionStatus()
        forPieMaking=(activity as MainActivity).mainActivityPieMaking
//        startPieChartmaking()


    }
    private fun checkurlDatabase() {

        var urlListsize=0
        chapterDatabaseViewModel.readAllChaptersViewModel.observe(fragmentLifecycleOwner!!,Observer{chapterList->
            Log.d("Home->checkurlDatabase()->chapterDatabaseViewModel","inside the chapterDatabaseViewModel")
            if(flag1==0){
                chapUrlroomViewModel.readAllChapterUrlViewModel.observe(fragmentLifecycleOwner!!,Observer { urlList ->
                    Log.d("Home->checkurlDatabase()->chapterDatabaseViewModel", "inside the chapUrlroomViewModel->${urlList.size}")
                    urlListsize=urlList.size
                    if(flag1==0){
                        flag1++
                        flagSharedEditor.apply{
                            putInt("flag1",1)
                            apply()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            networkCheck()
                            startPieChartmaking()
                                Log.d("Home->checkurlDatbase()","after the network check->${urlList.size}")
//                            delay(5000)
                                if (urlListsize == chapterList.size&&urlListsize!=0) {
                                    Log.d("Home->checkurlDatabase","urlList.size == chapterList.size ${urlList.size} ${chapterList.size}")
                                    getChapterFromDatabase()
                                } else {
                                    flag4=1
                                    Log.d("Home->checkurlDatabase","urlList.size != chapterList.size ${urlList.size} ${chapterList.size}")
                                    getAllGdgChapter(chapterList.size)
                                }
                            }
                        }
                })
            }
            else if(flag1==1&&flag4==0){
                Log.d("Home->checkurlDatabase","urlList.size != chapterList.size flag4==0")
                getAllGdgChapter(chapterList.size)
            }
            getChapterFromDatabase()
        })
    }

    fun networkCheck(){
        Log.d("Home->NetworkCheck()","network check called")
        if(gdgChaptersViewModel.isNetworkAvailable()){
                storeUrlinDatabase()
        }else {
            Toast.makeText(requireContext(), "No Internet Available", Toast.LENGTH_LONG).show()
        }
    }

     private fun startPieChartmaking(){
         Log.d("Home->startPieChartmaking()","Inside the startPieChartmaking")
        var countryList:ArrayList<GdgDataClass> = arrayListOf()
        if(forPieMaking==0||pieSharePref.all.size!=0){
            forPieMaking=1
            (activity as MainActivity).mainActivityPieMaking=1
            CoroutineScope(Dispatchers.IO).launch {
//                gdgChaptersViewModel.getChaptersViewModel()
                delay(3000)
                countryList=gdgChaptersList
                Log.d("Home->startPieChartmaking()","countryList->${countryList.size}")
                if (countryList.isNotEmpty()) {
                    mostChaptersCountry.clear()
                    for (i in 0 until countryList.size) {
                        increment(mostChaptersCountry, countryList[i].country)
                    }
                }
//                delay(5000)
                withContext(Dispatchers.Main){
                    setDataToPie()
//                    if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
                    if(loadingAnimation.visibility==View.GONE) {
                        loadingAnimation.visibility = View.VISIBLE
                        loadingAnimation.playAnimation()
                    }
                    statisticsImageView.visibility = View.VISIBLE
                    statisticsText.visibility = View.VISIBLE
                    secondcardview.visibility = View.VISIBLE
                    statisticsText.startAnimation(
                        AnimationUtils.loadAnimation(
                            binding.root.context,
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

        }else{
            setDataToPieFromPref()
        }
    }


//    private  fun startPieChartmaking() {
//        var countryList:List<ChaptersUrlEntity> = listOf()
//            if(forPieMaking==0){
//                forPieMaking=1
//                Log.d("startPieChartmaking","Pie making starting ")
//                        chapUrlroomViewModel..observe(
//                            fragmentLifecycleOwner!!,
//                            Observer {
//                                countryList = it
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    if (countryList.isNotEmpty()) {
//                                        mostChaptersCountry.clear()
//                                        for (i in 0 until countryList.size) {
//                                            increment(mostChaptersCountry, countryList[i].country)
//                                        }
//                                    }
//                                }
//                            })
//                        setDataToPie()
//                        if (progressBar.visibility == View.VISIBLE) progressBar.visibility =
//                        View.GONE
//                        statisticsImageView.visibility = View.VISIBLE
//                        statisticsText.visibility = View.VISIBLE
//                    secondcardview.visibility = View.VISIBLE
//                    statisticsText.startAnimation(
//                        AnimationUtils.loadAnimation(
//                            binding.root.context,
//                            android.R.anim.slide_in_left
//                        )
//                    )
//                    secondcardview.startAnimation(
//                        AnimationUtils.loadAnimation(
//                            requireContext(),
//                            android.R.anim.slide_in_left
//                        )
//                    )
//            } else {
//                setDataToPieFromPref()
//            }
//        }




    private fun setDataToPieFromPref() {
        Log.d("Home->startPieChartmaking()","Inside the startPieChartmaking")
        var j=0
        val listOfColor= listOf("#FFA726","#66BB6A","#EF5350","#29B6F6","#FFC8DD","#2B2D42","#8D99AE","#D5BDAF","#E4C1F9","#7400B8")
        counrtyList.add("All")
        if(pieSharePref.all.size==10) {
            pieSharePref.all.forEach { map ->
                pieChart.addPieSlice(
                    PieModel(
                        map.key,
                        map.value as Float,
                        Color.parseColor(listOfColor[j])
                    )
                )
                countryCount.add(
                    CountryCountData(
                        listOfColor[j],
                        map.key,
                        Integer.parseInt(map.value.toString().dropLast(2))
                    )
                )
                counrtyList.add(map.key)
                j++
            }
        }
        pieChartAdapter.refreshData(countryCount)
        countryAdapter.refreshData(counrtyList)
        statisticsText.visibility = View.VISIBLE
        secondcardview.visibility = View.VISIBLE
        statisticsImageView.visibility = View.VISIBLE

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
        loadConnectionStatus()
        setDataToPieFromPref()
        Log.d("Home->onResume","onResume() called")
        val customAppBar = (activity as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id
        if(isRootFragment){
            menuButton.setBackgroundResource(R.drawable.baseline_menu_24)
            menuButton.setOnClickListener{
                (activity as MainActivity).binding.drawerlayout.openDrawer(GravityCompat.START)
            }
        }else{
            menuButton.setOnClickListener{
                (activity)?.onBackPressed()
            }
        }
//            menuButton?.visibility = View.VISIBLE
//            backButton?.visibility = View.GONE
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
            act.binding.appBarMain.LGConnected.visibility=View.VISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.INVISIBLE
        } else {
            act.binding.appBarMain.LGConnected.visibility=View.INVISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.VISIBLE
        }
    }


    //Check if chapter_url database is empty then fetch data and if


    private fun getAllGdgChapter(j:Int) {
        Log.d("Home->getAllGdgChapter()", "beforedelay")
        val job4=CoroutineScope(Dispatchers.Main).launch {
           chapUrlroomViewModel.readAllChapterUrlViewModel.observe(fragmentLifecycleOwner!!,
               Observer { gdgURlChapterEntity ->
                   urlGetChapterList=gdgURlChapterEntity
                   Log.d("Home->getAllGdgChapter()", "urlGetChapterList size->${urlGetChapterList.size}")
                   if (flag3 == 0) {
                       flag3++
                       var i=j
                       Log.d("Home->getAllGdgChapter()", "starting from ${urlGetChapterList[j].url}")
                       CoroutineScope(Dispatchers.IO).launch {
                           while (i>=0) {
                               if(i==urlGetChapterList.size){
                                   break
                               }
                               gdgChaptersViewModel.getCompleteGDGdetails(urlGetChapterList[i].url)
                               val details = gdgChaptersViewModel.getdetails()
                               Log.d(
                                   "Home->getAllGdgChapter()",
                                   "${urlGetChapterList[i].url} gdgdetials response got with ${urlGetChapterList.size}"
                               )
                               chapterDatabaseViewModel.addChaptersViewModel(
                                   ChapterEntity(
                                       urlGetChapterList[i].avatar,
                                       urlGetChapterList[i].banner,
                                       urlGetChapterList[i].city,
                                       urlGetChapterList[i].city_name,
                                       urlGetChapterList[i].country,
                                       urlGetChapterList[i].latitude,
                                       urlGetChapterList[i].longitude,
                                       urlGetChapterList[i].url,
                                       details.gdgName,
                                       details.membersNumber,
                                       details.about
                                   )
                               )
                               Log.d(
                                   "Home->getAllGdgChapter()",
                                   "${urlGetChapterList[i].url} gdgdetials response added to database"
                               )
                               i++
                           }

                       }

               }
                })
            getChapterFromDatabase()

        }

    }

    private  fun setDataToPie() {

        var j=0
        val listOfColor= listOf("#FFA726","#66BB6A","#EF5350","#29B6F6","#FFC8DD","#2B2D42","#8D99AE","#D5BDAF","#E4C1F9","#7400B8")
        Log.d("Home->setDataToPie()",mostChaptersCountry.size.toString())

        if(mostChaptersCountry.size>11) {
            mostChaptersCountry.entries.sortedByDescending { it.value }.take(10)
                .forEach { sortedMap[it.key] = it.value }
        }
        counrtyList.add("All")
            sortedMap.forEach{(k,v)->Log.d("Home->setDataToPie()->sorted","$k->$v")}
            sortedMap.forEach { (k, v) ->
                pieEditor.apply{
                    pieEditor.putFloat(k, v.toFloat())
                    apply()
                }
                pieChart.addPieSlice(PieModel(k, v.toFloat(),Color.parseColor(listOfColor[j])))
                counrtyList.add(k)
                countryAdapter.refreshData(counrtyList)
                countryCount.add(CountryCountData(listOfColor[j],k,v))
                j++

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
//                    if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
                    if(loadingAnimation.visibility==View.VISIBLE) {
                        loadingAnimation.visibility = View.GONE
                    }
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
                                if (it.gdgName.lowercase(Locale.getDefault()).contains(searchtext)||
                                        it.country.lowercase(Locale.getDefault()).contains(searchtext)||
                                        it.city.lowercase(Locale.getDefault()).contains(searchtext)) {
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


                    countryAdapter.setOnItemClickListener(object :CounrtyAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            newadapterlist.clear()
                            val sortCountry = counrtyList[position]!!.toLowerCase(Locale.getDefault())
                            adapterlist.forEach {
                                if(sortCountry=="all"){
                                    newadapterlist.add(it)
                                }
                                else if (it.country.lowercase(Locale.getDefault()).contains(sortCountry)) {
                                    newadapterlist.add(it)
                                }
                            }
                            adapter.refreshData(newadapterlist)
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

    private  fun storeUrlinDatabase() {
        Log.d("Home->storeUrlinDatabase()", "before job1")
//        val job1=CoroutineScope(Dispatchers.IO).launch {
            Log.d("Home->storeUrlinDatabase()","Inside the Job1 started && before job2")
//            val job2=CoroutineScope(Dispatchers.IO).launch{
                Log.d("Home->storeUrlinDatabase","Inside the Job2 started ${Thread.currentThread().name}")
                gdgChaptersViewModel.getChaptersViewModel()
                Log.d("Home->storeUrlinDatabase","Inside the Job2 ended")
//            }
//            delay(3000)
//            job2.join()
//            job2.cancel()
            Log.d("Home->storeUrlinDatabase","After the Job2 join")
            gdgChaptersList=gdgChaptersViewModel.returnGDGChapterViewModel()
            val gdgChapterEntityList = convertGdgDataTypes(gdgChaptersList)
            Log.d("Home->storeUrlinDatabase","scarping list size ${gdgChapterEntityList.size}")
            for (chapter in gdgChapterEntityList) {
                chapUrlroomViewModel.addChapterUrlViewModel(chapter)
            }
            Log.d("Home->storeUrlinDatabase","After the Job2 join")
//        }
        Log.d("Home->storeUrlinDatabase","Inside the Job1 ended")
//        job1.join()
//        job1.cancel()

//        Log.d("Home->storeUrlinDatabase","job1 is ${job1.isActive} || ${job1.isCancelled}||${job1.isCompleted}")
//        Log.d("Home->storeUrlinDatabase","job1 is ${job1.isActive} || ${job1.isCancelled}||${job1.isCompleted}")
        Log.d("Home->storeUrlinDatabase"," after the Job1 join")
    }

    private fun convertGdgDataTypes(gdgChaptersList: ArrayList<GdgDataClass>):ArrayList<ChaptersUrlEntity> {
        val chaptersUrlEntityList= ArrayList<ChaptersUrlEntity>()
        for(chapter in gdgChaptersList){
            val gdgchapter= ChaptersUrlEntity(
                chapter.avatar,
                chapter.banner,
                chapter.city_name,
                chapter.city,
                chapter.country,
                chapter.latitude,
                chapter.longitude,
                chapter.url
            )
            chaptersUrlEntityList.add(gdgchapter)
        }
        return chaptersUrlEntityList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chapterDatabaseViewModel.readAllChaptersViewModel.removeObserver{fragmentLifecycleOwner!!}
        chapUrlroomViewModel.readAllChapterUrlViewModel.removeObserver{fragmentLifecycleOwner!!}
    }
    private fun startFlyingAnimation(container: ImageView) {
        val radius = container.width/3f
        val centerX = container.width / 2f
        val centerY = container.height.toFloat()

        val totalObjects = objectsToAnimate.size
        val angleIncrement = Math.PI.toFloat() / totalObjects.toFloat()

        val animatorSet = AnimatorSet()

        val animators = mutableListOf<ObjectAnimator>()
        for ((index, view) in objectsToAnimate.withIndex()) {
            val angle = angleIncrement * index
            val x = centerX + radius * Math.cos(angle.toDouble()).toFloat()
            val y = centerY - radius * Math.sin(angle.toDouble()).toFloat()

            val animatorX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.translationX, x)
            val animatorY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.translationY, y)

            animators.add(animatorX)
            animators.add(animatorY)
        }

        animatorSet.playTogether(animators as MutableCollection<Animator>)
        animatorSet.duration = 3000
        animatorSet.start()
    }


}