package com.example.googledeveloperscommunityvisualisationtool.fragments.settings

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
import androidx.navigation.fragment.findNavController
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentSettingsBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapViewModelFact
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroomfactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroommodel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomfactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventModelFact
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventRoomFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel
import com.example.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSettingsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        ttsSwitch=binding.ttsSwitch
        fifthCardView=binding.FifthcardView
        fourthCardView=binding.FourthcardView
        sharedPref=activity?.getSharedPreferences("Theme",Context.MODE_PRIVATE)!!
        prefEditor=sharedPref.edit()
        val night=sharedPref.getBoolean("Night",false)

        if(night){
            binding.themeMode.isChecked=true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        ttsSharedPreferences=activity?.getSharedPreferences("TextToSpeechSwitch",Context.MODE_PRIVATE)!!
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
        binding.ThirdcardView.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_alarm_notification)
        }
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
        val customAppBar = (activity as MainActivity).binding.appBarMain
        val menuButton = customAppBar.menuButton
        val backButton = customAppBar.backarrow

        val navController = findNavController()
        val isRootFragment = navController.graph.startDestinationId == navController.currentDestination?.id

        if (isRootFragment) {
            menuButton?.visibility = View.VISIBLE
            backButton?.visibility = View.GONE
        } else {
            menuButton?.visibility = View.GONE
            backButton?.visibility = View.VISIBLE
        }

        backButton?.setOnClickListener {
            (activity as MainActivity).onBackPressed()
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
            act.binding.appBarMain.connectionStatus.text=resources.getString(R.string.connected)
            act.binding.appBarMain.connectionStatus.setTextColor(resources.getColor(R.color.Connected))
        } else {
            act.binding.appBarMain.connectionStatus.text=resources.getString(R.string.not_connected)
            act.binding.appBarMain.connectionStatus.setTextColor(resources.getColor(R.color.NotConnected))

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        gdgChapterRoomModel=ViewModelProvider(requireActivity(),
            ChapViewModelFact(requireContext())
        ).get(ChapterViewModel::class.java)
        chapterUrlRoomModel=ViewModelProvider(requireActivity(), ChapUrlroomfactory(requireContext())).get(
            ChapUrlroomViewModel::class.java)
        lasteWeekRoomModel=ViewModelProvider(requireActivity(), lastweekroomfactory(requireContext())).get(
            lastweekroommodel::class.java)
        lastWeekEventRoomModel=ViewModelProvider(requireActivity(),
            LastEventModelFact(requireContext())
        ).get(LastEventViewModel::class.java)
        notificatioModel=ViewModelProvider(requireActivity(), NotifyViewModelFactory(requireContext())).get(
            NotifyViewModel::class.java)
        oldGDGRoomViewModel=ViewModelProvider(requireActivity(), oldGDGroomFactory(requireContext())).get(
            oldGDGroomViewModel::class.java)
        upcomingEventRoomModel=ViewModelProvider(requireActivity(), UpcoEventRoomFactory(requireContext())).get(
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

        Toast.makeText(requireContext(),"ALl Databases are cleared! Now fresh data will be downloaded",
            Toast.LENGTH_LONG).show()
    }
    private fun showAlertDialog() {
        val dialogView = layoutInflater.inflate(R.layout.clean_database_alert, null)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
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