package com.example.googledeveloperscommunityvisualisationtool.fragments.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.googledeveloperscommunityvisualisationtool.R
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
            findNavController().navigate(R.id.action_settings_to_connection)
        }
        binding.ThirdcardView.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_alarm_notification)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}