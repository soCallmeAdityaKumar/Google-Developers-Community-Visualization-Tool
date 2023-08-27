package com.aditya.googledeveloperscommunityvisualisationtool.fragments.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.aditya.googledeveloperscommunityvisualisationtool.MainActivity
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentSettingsBinding
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapViewModelFact
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroomfactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroommodel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomfactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventModelFact
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModel
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModelFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventRoomFactory
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.google.android.material.card.MaterialCardView
import java.util.Locale

class Settings : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding:FragmentSettingsBinding
    private lateinit var sharedPref:SharedPreferences
    private lateinit var prefEditor:SharedPreferences.Editor
    private lateinit var arrayAdapter:ArrayAdapter<String>
    private lateinit var fourthCardView: MaterialCardView
    private lateinit var ttsSharedPreferences: SharedPreferences
    private lateinit var ttsEditor:SharedPreferences.Editor
    lateinit var fifthCardView:MaterialCardView
    lateinit var gdgChapterRoomModel:ChapterViewModel
    lateinit var chapterUrlRoomModel: ChapUrlroomViewModel
    lateinit var lasteWeekRoomModel:lastweekroommodel
    lateinit var lastWeekEventRoomModel:LastEventViewModel
    lateinit var notificatioModel:NotifyViewModel
    lateinit var oldGDGRoomViewModel:oldGDGroomViewModel
    lateinit var upcomingEventRoomModel:UpcoEventroomViewmodel
    private lateinit var ttsSwitch:Switch
    lateinit var activ: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!requireActivity().isFinishing && !requireActivity().isDestroyed) {
            activ = requireActivity()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSettingsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        ttsSwitch=binding.ttsSwitch
        fifthCardView=binding.FifthcardView
        fourthCardView=binding.FourthcardView
        sharedPref=activ.getSharedPreferences("Theme",Context.MODE_PRIVATE)
        prefEditor=sharedPref.edit()
        val night=sharedPref.getBoolean("Night",false)

        if(night){
            binding.themeMode.isChecked=true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        ttsSharedPreferences=activ.getSharedPreferences("TextToSpeechSwitch",Context.MODE_PRIVATE)
        ttsEditor=ttsSharedPreferences.edit()
        val ttsOn=ttsSharedPreferences.getBoolean("TTS",false)

        if(ttsOn){
            ttsSwitch.isChecked=true
        }
        ttsSwitch.setOnCheckedChangeListener{buttonView,isChecked->
            if(!isChecked){
                ttsEditor.putBoolean("TTS",false)
                ttsEditor.apply()
            }else{
                ttsEditor.putBoolean("TTS",true)
                ttsEditor.apply()
            }
        }


        binding.FirstcardView.setOnClickListener{
            findNavController().navigate(R.id.action_settings_to_connection)
        }
//        binding.ThirdcardView.setOnClickListener {
//            findNavController().navigate(R.id.action_settings_to_alarm_notification)
//        }
        fourthCardView.setOnClickListener{
            findNavController().navigate(R.id.action_settings_to_lgTask)
        }


        binding.themeMode.setOnCheckedChangeListener{buttonView,isChecked->
            if(!isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefEditor.putBoolean("Night",false)
                prefEditor.apply()
//                val mainActivity=activity as MainActivity
//                mainActivity.binding.drawerlayout.setBackgroundResource(R.drawable.light_background)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefEditor.putBoolean("Night",true)
                prefEditor.apply()
//                val mainActivity=activity as MainActivity
//                mainActivity.binding.drawerlayout.setBackgroundResource(R.drawable.dark_background)
            }
        }
        fifthCardView.setOnClickListener {
            showAlertDialog()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadConnectionStatus()


        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        val customAppBar = (activ as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton
        menuButton.setBackgroundResource(R.drawable.backarrow)
//            menuButton?.visibility = View.GONE
//            backButton?.visibility = View.VISIBLE
        menuButton?.setOnClickListener{
            (activity)?.onBackPressed()
        }

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        gdgChapterRoomModel=ViewModelProvider(activ,
            ChapViewModelFact(requireContext())
        ).get(ChapterViewModel::class.java)
        chapterUrlRoomModel=ViewModelProvider(activ, ChapUrlroomfactory(requireContext())).get(
            ChapUrlroomViewModel::class.java)
        lasteWeekRoomModel=ViewModelProvider(activ, lastweekroomfactory(requireContext())).get(
            lastweekroommodel::class.java)
        lastWeekEventRoomModel=ViewModelProvider(requireActivity(),
            LastEventModelFact(requireContext())
        ).get(LastEventViewModel::class.java)
        notificatioModel=ViewModelProvider(activ, NotifyViewModelFactory(requireContext())).get(
            NotifyViewModel::class.java)
        oldGDGRoomViewModel=ViewModelProvider(activ, oldGDGroomFactory(requireContext())).get(
            oldGDGroomViewModel::class.java)
        upcomingEventRoomModel=ViewModelProvider(activ, UpcoEventRoomFactory(requireContext())).get(
            UpcoEventroomViewmodel::class.java)

    }
    private fun deleteAllDatabase() {
        gdgChapterRoomModel.deleteAllChapterViewModel()
        chapterUrlRoomModel.deleteAllChapterUrlViewModel()
        lastWeekEventRoomModel.deleteAllevent()
        lasteWeekRoomModel.deleteAlllastWeekViewModel()
        notificatioModel.deleteAllNotification()
        oldGDGRoomViewModel.deleteAllOldGDGChapterModel()
        upcomingEventRoomModel.deleteAllevent()
        val edit=activ.getSharedPreferences("Flags",Context.MODE_PRIVATE)!!.edit()
        edit.apply{
            putInt("flag1",0)
        }

        Toast.makeText(requireContext(),"ALl Databases are cleared! Now fresh data will be downloaded",
            Toast.LENGTH_LONG).show()
    }
    private fun showAlertDialog() {
        val dialogView = layoutInflater.inflate(R.layout.clean_database_alert, null)
//        dialogView.setBackgroundResource(android.R.color.transparent)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView.findViewById<AppCompatButton>(R.id.confirm).setOnClickListener {
            deleteAllDatabase()
            alertDialog.cancel()
        }
        dialogView.findViewById<AppCompatButton>(R.id.Cancel).setOnClickListener{
            alertDialog.cancel()
        }

        alertDialog.show()
    }

}