package com.example.googledeveloperscommunityvisualisationtool.fragments.settings

import android.app.Activity
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentSettingsBinding
import java.util.Locale

class Settings : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding:FragmentSettingsBinding
    private lateinit var sharedPref:SharedPreferences
    private lateinit var prefEditor:SharedPreferences.Editor
    private lateinit var arrayAdapter:ArrayAdapter<String>
    private lateinit var ttsSharedPreferences: SharedPreferences
    private lateinit var ttsEditor:SharedPreferences.Editor
    private lateinit var ttsSwitch:Switch
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSettingsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        ttsSwitch=binding.ttsSwitch
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

        return view
    }
    override fun onResume() {
        super.onResume()
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
    object LocaleHelper{
        fun setLocale(activity: Activity,languageCode:String){
            val locale= Locale(languageCode)
            Locale.setDefault(locale)
            val configuration= Configuration()
            configuration.setLocale(locale)
            activity.baseContext.resources.updateConfiguration(configuration,activity.baseContext.resources.displayMetrics)
        }
    }
    private fun updateAppLanguage(languageCode: String) {
        // Use the LocaleHelper from the previous example to change the app's language
        LocaleHelper.setLocale(requireActivity(), languageCode)

    }

    private fun getLanguageCodeFromPosition(position: Int): String {
        // Map the position to the corresponding language code
        return when (position) {
            0 -> "en"
            1 -> "hi"
            else -> "en"
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}