package com.example.googledeveloperscommunityvisualisationtool.Fragments.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentMainSettingsBinding

class MainSettings : Fragment() {
    lateinit var binding:FragmentMainSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMainSettingsBinding.inflate(layoutInflater, container, false)
        val view=binding.root
        return view
    }


}

