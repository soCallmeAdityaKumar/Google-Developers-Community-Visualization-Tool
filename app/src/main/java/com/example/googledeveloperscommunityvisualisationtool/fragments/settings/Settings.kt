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
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentSettingsBinding
import java.util.Locale

class Settings : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding:FragmentSettingsBinding
    private lateinit var sharedPref:SharedPreferences
    private lateinit var prefEditor:SharedPreferences.Editor
    private lateinit var arrayAdapter:ArrayAdapter<String>
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

        val languages=resources.getStringArray(R.array.languages)
        arrayAdapter=
            ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,languages)
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
                val selectedLanguageCode = getLanguageCodeFromPosition(position)
                updateAppLanguage(selectedLanguageCode)
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