package com.example.googledeveloperscommunityvisualisationtool.fragments.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Bitmap.Config
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.sqlite.db.SupportSQLiteOpenHelper
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
    private lateinit var languageList:List<String>
    private lateinit var spinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSettingsBinding.inflate(layoutInflater,container,false)
        val view=binding.root
        spinner=binding.LanguageSpinner

        sharedPref=activity?.getSharedPreferences("Theme",Context.MODE_PRIVATE)!!
        prefEditor=sharedPref.edit()
        val night=sharedPref.getBoolean("Night",false)

        if(night){
            binding.themeMode.isChecked=true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
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

        languageList= listOf("Select Langauage","English","Hindi")
        arrayAdapter=
            ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,languageList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=arrayAdapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                val selectedLanguageCode = getLanguageCodeFromPosition(position)
//                LocaleHelper.setLocale(requireActivity(),selectedLanguageCode)
                val selectedLanguage=parent!!.getItemAtPosition(position).toString()
                if(selectedLanguage.equals("English")){
                    LocaleHelper.setLocale(requireActivity(),selectedLanguage)
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                }else if(selectedLanguage.equals("Hindi")){
                    LocaleHelper.setLocale(requireActivity(),selectedLanguage)
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        return view
    }
    object LocaleHelper{
        fun setLocale(activity: Activity,languageCode:String){
            val locale= Locale(languageCode)
            Locale.setDefault(locale)
            val configuration= Configuration()
            configuration.setLocale(locale)
            activity.baseContext.resources.updateConfiguration(configuration,activity.baseContext.resources.displayMetrics)
//            activity.recreate()
        }
    }

    private fun getLanguageCodeFromPosition(position: Int): String {
        // Map the position to the corresponding language code
        return when (position) {
            0 -> "en" // English
            1 -> "hi" // French
            // Add more cases for other languages as needed
            else -> "en" // Default to English
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}