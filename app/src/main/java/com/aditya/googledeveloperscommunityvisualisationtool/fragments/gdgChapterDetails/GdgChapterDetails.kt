package com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.TextToSpeech.TextToSpeechClass
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.connection.LGConnectionTest.testPriorConnection
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgChapModelFactory
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgScrapingRespo
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters.GdgViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.GDGDetails
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.PastEvents
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.UpcomingEvents
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.airbnb.lottie.LottieAnimationView
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentGdgChapterDetailsBinding
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.GdgChapterDetailsArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean


class GdgChapterDetails : Fragment() {
    val args: GdgChapterDetailsArgs by navArgs()
    lateinit var gdgName:TextView
    lateinit var cityName:TextView
    lateinit var countryName:TextView
    lateinit var aboutGdg:TextView
    lateinit var organizerRecyclerView:RecyclerView
    lateinit var upcomingEventsRecycler:RecyclerView
    lateinit var pasteventsRecycler:RecyclerView
    lateinit var pastEventsList:List<events>
    lateinit var tourGDG:TourGDGThread
    lateinit var oribitGDG:OrbitGDGThread
    lateinit var upcomingEventlist:List<events>
    lateinit var eventsAdapterpast: pastEventAdapter
    lateinit var upcoEventsAdapterupcoming: UpcoEventsAdapter
    lateinit var binding: FragmentGdgChapterDetailsBinding
    lateinit var gdgViewModel: GdgViewModel
    lateinit var gdgDetails: GDGDetails
    lateinit var member:TextView
    lateinit var organizerAdapter:OrganizersAdapter
    lateinit var organizerList:List<Organizers>
    lateinit var noUpcomingEventTextView:TextView
    lateinit var startOrbitGdgButton:Button
    lateinit var stopOrbitGgdButton:Button
    lateinit var sharedPref:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var progressBar: ProgressBar
    lateinit var scrollView: ScrollView
    lateinit var loadingLottieAnimationView: LottieAnimationView
    lateinit var aboutgdgcardView:CardView
    lateinit var organizercardView:CardView
    lateinit var upcomingcardView:CardView
    lateinit var facebookLogo:CircleImageView
    lateinit var twitterLogo:CircleImageView
    lateinit var linkedInLogo:CircleImageView
    lateinit var gmailLogo:CircleImageView
    lateinit var instagramLogo:CircleImageView
    lateinit var textToSpeech: TextToSpeechClass
    lateinit var ttsSharedPreferences: SharedPreferences

    var handler=Handler()

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=FragmentGdgChapterDetailsBinding.inflate(layoutInflater,container, false)
         val view=binding.root


         progressBar=binding.progressBar
         scrollView=binding.scrollView

//         progressBar.visibility=View.VISIBLE
         scrollView.visibility=View.GONE

         textToSpeech= TextToSpeechClass(requireContext())
         ttsSharedPreferences=activity?.getSharedPreferences("TextToSpeechSwitch",Context.MODE_PRIVATE)!!
             val code=textToSpeech.initialise()
             if(code== TextToSpeech.SUCCESS){
                 Log.d("tts","enabled")
             }
             else{
                 Log.d("tts","Not enabled")
             }



         gdgName=binding.gdgname
         cityName=binding.cityname
         countryName=binding.countryname
         aboutGdg=binding.aboutgdg
         member=binding.memebers
         aboutgdgcardView=binding.aboutgdgcardview
         organizerRecyclerView=binding.orgainzersrecycler
         pasteventsRecycler=binding.pasteventsrecycler
         upcomingEventsRecycler=binding.upcomingrecyclerview
         loadingLottieAnimationView=binding.loadinLottieAnimation
         linkedInLogo=binding.linkedin
         facebookLogo=binding.facebook
         twitterLogo=binding.twitter
         gmailLogo=binding.gmail
         instagramLogo=binding.instagram


         gdgName.visibility=View.GONE
         cityName.visibility=View.GONE
         countryName.visibility=View.GONE
         member.visibility=View.GONE
         pasteventsRecycler.visibility=View.GONE
         upcomingEventsRecycler.visibility=View.GONE
         aboutgdgcardView.visibility=View.GONE
         organizerRecyclerView.visibility=View.GONE
         loadingLottieAnimationView.visibility=View.VISIBLE
         loadingLottieAnimationView.playAnimation()



         linkedInLogo.setBackgroundResource(R.drawable.linkedin_icon)
         facebookLogo.setBackgroundResource(R.drawable.facebook_logo)
         gmailLogo.setBackgroundResource(R.drawable.gmail_icons)
         twitterLogo.setBackgroundResource(R.drawable.twitter_logo)
         instagramLogo.setBackgroundResource(R.drawable.instagram_icon)

         organizerRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         organizerList= listOf()
         organizerAdapter= OrganizersAdapter(organizerList)
         organizerRecyclerView.adapter=organizerAdapter

         upcomingEventlist= listOf()
         upcomingEventsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         upcoEventsAdapterupcoming=UpcoEventsAdapter(upcomingEventlist)
         upcomingEventsRecycler.adapter=upcoEventsAdapterupcoming
         upcomingEventsRecycler.visibility=View.GONE

         noUpcomingEventTextView=binding.NoUpcomingView
         noUpcomingEventTextView.visibility=View.VISIBLE


         pastEventsList= listOf()
         pasteventsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         eventsAdapterpast= pastEventAdapter(pastEventsList)
         pasteventsRecycler.adapter=eventsAdapterpast

         startOrbitGdgButton=binding.StartOrbitGdgButton
         stopOrbitGgdButton=binding.StopOrbitGdgButton

         startOrbitGdgButton.setOnClickListener { orbit() }
         startOrbitGdgButton.setOnClickListener { orbit() }


        return view
    }

    private fun stopOrbit() {
    }

    private fun orbit() {
        Log.d("Orbit","Inside the orbit ")
        val isConnected = AtomicBoolean(false)
        testPriorConnection(requireActivity(), isConnected)
        val sharedPreferences = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)
        handler.postDelayed({
            if (isConnected.get()) {
                oribitGDG = OrbitGDGThread(
                    args.chapter,
                )
                oribitGDG!!.start()
            }
            loadConnectionStatus(sharedPreferences!!)
        }, 1200)
    }

    override fun onResume() {
        super.onResume()
        loadConnectionStatus()

        CoroutineScope(Dispatchers.IO).launch {
            getDetails()
        }
        tour()

        val customAppBar = (activity as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton.setBackgroundResource(R.drawable.baseline_menu_24)
//            menuButton?.visibility = View.VISIBLE
//            backButton?.visibility = View.GONE
        } else {
            menuButton.setBackgroundResource(R.drawable.backarrow)
//            menuButton?.visibility = View.GONE
//            backButton?.visibility = View.VISIBLE
            menuButton?.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }
        }
    }
    private fun loadConnectionStatus() {
        val sharedPreferences = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)

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

    private fun stopTour() {
        tourGDG!!.stop()
    }

    private fun tour() {
        val isConnected = AtomicBoolean(false)
        testPriorConnection(requireActivity(), isConnected)
        val sharedPreferences = activity?.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name, MODE_PRIVATE)
        handler.postDelayed({
            if (isConnected.get()) {
                try {
                    val lgConnectionManager = LGConnectionManager.getInstance()
                    lgConnectionManager!!.startConnection()
                    val lgCommand = LGCommand(
                        ActionBuildCommandUtility.buildCommandCleanSlaves(),
                        LGCommand.CRITICAL_MESSAGE, object : LGCommand.Listener {
                            override fun onResponse(response: String?) {

                            }
                        })
                    lgConnectionManager.addCommandToLG(lgCommand)
                }catch (e:Exception){
                    println("Could not connect to LG")
                }
//                showDialog(requireActivity(), "Starting the GDG TOUR")
                tourGDG = TourGDGThread(
                    args.chapter as ChapterEntity,
                    requireActivity(),
                )
                tourGDG!!.start()
            }
            loadConnectionStatus(sharedPreferences!!)
        }, 1200)
    }
    private fun loadConnectionStatus(sharedPreferences: SharedPreferences) {
        val isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name, false)
        val act=activity as MainActivity
        if (isConnected!!) {
            act.binding.appBarMain.LGConnected.visibility=View.VISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.INVISIBLE
        } else {
            act.binding.appBarMain.LGConnected.visibility=View.INVISIBLE
            act.binding.appBarMain.LGNotConnected.visibility=View.VISIBLE
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val gdgChapterRepo= GdgScrapingRespo()
        gdgViewModel = ViewModelProvider(this, GdgChapModelFactory(gdgChapterRepo, requireContext())).get(GdgViewModel::class.java)

        Log.d("content","content visible")

    }


    private suspend fun getDetails() {
        val store=(activity as MainActivity)
        val gson = Gson()
        sharedPref=activity?.getSharedPreferences(args.chapter.gdgName,Context.MODE_PRIVATE)!!
        editor=sharedPref.edit()
        val job=CoroutineScope(Dispatchers.IO).launch {
            gdgViewModel.getCompleteGDGdetails(args.chapter.url)
        }

        if(sharedPref.getString("gdgname",null)==args.chapter.gdgName) {
            Log.d("storedargs",args.chapter.gdgName)
            Log.d("stored",sharedPref.getString("gdgname",null).toString())
            gdgName.text = sharedPref.getString("gdgname", null)
            cityName.text =" ${sharedPref.getString("cityname", null)}, ${sharedPref.getString("countryname", null)}"
            aboutGdg.text = sharedPref.getString("aboutgdg", null)
            member.text = sharedPref.getString("member", null)

             val stringorganizer:String= sharedPref.getString("organzierslist","")!!
             val stringpastevent:String= sharedPref.getString("pasteventslist","")!!.replace(Regex("&quot;"),"")
            val stringupcoming= sharedPref.getString("upcomingeventlist","")!!


            val organizerlistType=object :TypeToken<List<Organizers>>() {}.type
            val eventlistType=object :TypeToken<List<events>>() {}.type

            val pastEventsList:List<events> =gson.fromJson(stringpastevent,eventlistType)
            val upcomingEventlist:List<events> =gson.fromJson(stringupcoming,eventlistType)
           val  organizerList:List<Organizers> =gson.fromJson(stringorganizer,organizerlistType)

            Log.d("stored",pastEventsList.size.toString())

            organizerAdapter.refreshData(organizerList)
            eventsAdapterpast.refreshData(pastEventsList)
            if(sharedPref.getString("facebook","")!!.isNotEmpty()){
                facebookLogo.setOnClickListener{
                    val uri = Uri.parse(sharedPref.getString("facebook",""))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }else{
                facebookLogo.visibility=View.GONE
            }
            if(sharedPref.getString("instagram","")!!.isNotEmpty()){
                instagramLogo.setOnClickListener{
                    val uri = Uri.parse(sharedPref.getString("instagram",""))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }else{
                instagramLogo.visibility=View.GONE
            }
            if(sharedPref.getString("linkedIn","")!!.isNotEmpty()){
                linkedInLogo.setOnClickListener{
                    val uri = Uri.parse(sharedPref.getString("linkedIn",""))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }else{
                linkedInLogo.visibility=View.GONE
            }
            if(sharedPref.getString("twitter","")!!.isNotEmpty()){
                twitterLogo.setOnClickListener{
                    val uri = Uri.parse(sharedPref.getString("twitter",""))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }else{
                twitterLogo.visibility=View.GONE
            }
            if(sharedPref.getString("email","")!!.isNotEmpty()){
                gmailLogo.setOnClickListener{
                    val uri = Uri.parse(sharedPref.getString("email",""))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }else{
                gmailLogo.visibility=View.GONE
            }

            //make views visible
            withContext(Dispatchers.Main) {
                gdgName.visibility = View.VISIBLE
                gdgName.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                cityName.visibility = View.VISIBLE
                cityName.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                countryName.visibility = View.VISIBLE
                countryName.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                member.visibility = View.VISIBLE
                member.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                pasteventsRecycler.visibility = View.VISIBLE
                pasteventsRecycler.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                upcomingEventsRecycler.visibility = View.VISIBLE
                upcomingEventsRecycler.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                aboutgdgcardView.visibility = View.VISIBLE
                aboutgdgcardView.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    )
                )
                organizerRecyclerView.visibility = View.VISIBLE
                organizerRecyclerView.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        android.R.anim.slide_in_left
                    ),
                )
            }
            withContext(Dispatchers.Main){
                if(upcomingEventlist.size!=0){
                    upcoEventsAdapterupcoming.refreshData(upcomingEventlist)
                    upcoEventsAdapterupcoming.setOnItemClickListener(object :
                        UpcoEventsAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val uri = Uri.parse(upcomingEventlist[position].link)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }

                    })
                    upcomingEventsRecycler.visibility = View.VISIBLE
                    noUpcomingEventTextView.visibility = View.GONE
                }else{
                    noUpcomingEventTextView.visibility = View.VISIBLE
                    upcomingEventsRecycler.visibility = View.GONE
                }
//                if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
                if (loadingLottieAnimationView.visibility == View.VISIBLE) loadingLottieAnimationView.visibility = View.GONE
                if (scrollView.visibility == View.GONE) {
                scrollView.visibility = View.VISIBLE
                }
                if(ttsSharedPreferences.getBoolean("TTS",false)) {
                    speakOut()
                }
            }
        }
        else {
            job.join()
            withContext(Dispatchers.Main) {

                gdgDetails = gdgViewModel.getdetails()
                gdgName.text=gdgDetails.gdgName
                cityName.text = "${args.chapter.city_name}, ${args.chapter.country}"
                aboutGdg.text = args.chapter.about
                member.text =args.chapter.membersNumber
                organizerList = gdgDetails.orgnaizersList
                pastEventsList = coneverttoeventsbypast(gdgDetails.pastEventsList)
                upcomingEventlist = coneverttoeventsbyupcoming(gdgDetails.upcomingEventsList)

                if(gdgDetails.facebookLink.isNotEmpty()){
                    facebookLogo.setOnClickListener{
                        val uri = Uri.parse(gdgDetails.facebookLink)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }else{
                    facebookLogo.visibility=View.GONE
                }
                if(gdgDetails.instagramLink.isNotEmpty()){
                    instagramLogo.setOnClickListener{
                        val uri = Uri.parse(gdgDetails.instagramLink)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }else{
                    instagramLogo.visibility=View.GONE
                }
                if(gdgDetails.linkedIn.isNotEmpty()){
                    linkedInLogo.setOnClickListener{
                        val uri = Uri.parse(gdgDetails.linkedIn)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }else{
                    linkedInLogo.visibility=View.GONE
                }
                if(gdgDetails.twitterLink.isNotEmpty()){
                    twitterLogo.setOnClickListener{
                        val uri = Uri.parse(gdgDetails.twitterLink)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }else{
                    twitterLogo.visibility=View.GONE
                }
                if(gdgDetails.emailLink.isNotEmpty()){
                    gmailLogo.setOnClickListener{
                        val uri = Uri.parse(gdgDetails.emailLink)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }else{
                    gmailLogo.visibility=View.GONE
                }
                if (store.storedgdgData<2) {
                    store.storedgdgData+=1
                    storeDetailsinPref()
                    editor.apply {
                        putString("gdgname", gdgDetails.gdgName)
                        putString("cityname", args.chapter.city_name)
                        putString("countryname", args.chapter.country)
                        Log.d("store", "stored - ${gdgDetails.gdgName}")
                        putString("aboutgdg", gdgDetails.about)
                        putString("member", gdgDetails.membersNumber)
                        putString("instagram",gdgDetails.instagramLink)
                        putString("facebook",gdgDetails.facebookLink)
                        putString("twitter",gdgDetails.twitterLink)
                        putString("linkedIn",gdgDetails.linkedIn)
                        putString("email",gdgDetails.emailLink)


                        val stringorganizer = gson.toJson(organizerList)
                         val stringpastevent = gson.toJson(pastEventsList)
                         val stringupcoming = gson.toJson(upcomingEventlist)


                        putString("organzierslist", stringorganizer)
                        putString("pasteventslist", stringpastevent)
                        putString("upcomingeventlist", stringupcoming)
                        apply()
                    }
                }

                Log.d("upcomingeventinactivechapter", gdgDetails.upcomingEventsList.size.toString())
                if (upcomingEventlist.size != 0) {

                    upcoEventsAdapterupcoming.refreshData(upcomingEventlist)
                    upcoEventsAdapterupcoming.setOnItemClickListener(object :
                        UpcoEventsAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val uri = Uri.parse(upcomingEventlist[position].link)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }

                    })
                    upcomingEventsRecycler.visibility = View.VISIBLE
                    noUpcomingEventTextView.visibility = View.GONE
                } else {
                    noUpcomingEventTextView.visibility = View.VISIBLE
                    upcomingEventsRecycler.visibility = View.GONE
                }

                for (i in upcomingEventlist) {
                    Log.d("hello", i.toString())
                }
                eventsAdapterpast.refreshData(pastEventsList)

                organizerAdapter.refreshData(organizerList)
//                if (progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
                if (loadingLottieAnimationView.visibility == View.VISIBLE) loadingLottieAnimationView.visibility = View.GONE
                if (scrollView.visibility == View.GONE) {
                    scrollView.visibility = View.VISIBLE
                }


                    gdgName.visibility = View.VISIBLE
                    gdgName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    cityName.visibility = View.VISIBLE
                    cityName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    countryName.visibility = View.VISIBLE
                    countryName.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    member.visibility = View.VISIBLE
                    member.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                pasteventsRecycler.visibility = View.VISIBLE
                pasteventsRecycler.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                upcomingEventsRecycler.visibility = View.VISIBLE
                upcomingEventsRecycler.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                    aboutgdgcardView.visibility = View.VISIBLE
                    aboutgdgcardView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        )
                    )
                organizerRecyclerView.visibility = View.VISIBLE
                organizerRecyclerView.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            android.R.anim.slide_in_left
                        ),
                )
                if(ttsSharedPreferences.getBoolean("TTS",false)) {
                    speakOut()
                }


            }
        }


    }

    private fun storeDetailsinPref() {

    }

    private fun speakOut() {
        textToSpeech.speakText(gdgName.text.toString())
        textToSpeech.speakText(cityName.text.toString())
        textToSpeech.speakText(countryName.text.toString())
        textToSpeech.speakText(member.text.toString())
        textToSpeech.speakText("About ${gdgName.text.toString()}")
        textToSpeech.speakText(aboutGdg.text.toString())
        for(i in organizerList){
            textToSpeech.speakText(i.organizername)
            textToSpeech.speakText(i.organizercompany)
            textToSpeech.speakText(i.organizerTitle)
        }
        if(upcomingEventlist.size!=0){
            textToSpeech.speakText(upcomingEventlist[0].title)
            textToSpeech.speakText(upcomingEventlist[0].date)
        }else{
            textToSpeech.speakText("There are no upcoming events in this gdg by now")
        }

        textToSpeech.speakText("There is ${pastEventsList.size.toString()} past events by now")
    }


    private fun coneverttoeventsbypast(pastEventsList: List<PastEvents>): List<events> {
        val events= mutableListOf<events>()
        for( i in pastEventsList){
            val event=events(i.pastEventstitle,i.pastEventsdate,i.pastEventslink,i.pastEventstype)
            events.add(event)
        }
        return events
    }
    override fun onDestroy() {
        textToSpeech.speechStop()
        super.onDestroy()
    }

    private fun coneverttoeventsbyupcoming(upEventsList: List<UpcomingEvents>): List<events> {
        val events= mutableListOf<events>()
        for( i in upEventsList){
            val event=events(i.upcomingEventstitle,i.upcomingEventsdate,i.upcomingEventslink,i.upcomingEventsdescription)
            events.add(event)
        }
        return events
    }

}