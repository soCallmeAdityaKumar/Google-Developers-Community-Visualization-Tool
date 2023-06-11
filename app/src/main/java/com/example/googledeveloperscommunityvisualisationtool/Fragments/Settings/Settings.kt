package com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.AlarmNotification.Alarm_notification
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings.Connection.connection
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentMapsBinding
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentSettingsBinding

class Settings : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding:FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSettingsBinding.inflate(layoutInflater,container,false)
        val view=binding.root



        binding.FirstcardView.setOnClickListener{
//            val fragmentManager=
//            val fragmenttransaction=fragmentManager.beginTransaction()
//            fragmenttransaction.replace(R.id.linearlayout,connection())
//            fragmenttransaction.commit()
//            findNavController()?.navigate(R.id.action_settings3_to_connection)
        }
        binding.ThirdcardView.setOnClickListener {
//            val fragmentManager=parentFragmentManager
//            val fragmenttransaction=fragmentManager.beginTransaction()
//            fragmenttransaction.replace(R.id.linearlayout,Alarm_notification())
//            fragmenttransaction.commit()
//            findNavController()?.navigate(R.id.action_settings3_to_alarm_notification)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}