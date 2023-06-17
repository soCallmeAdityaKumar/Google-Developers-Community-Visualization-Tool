package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentOldEventBinding

class OldEvent : Fragment() {
    lateinit var binding: FragmentOldEventBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOldEventBinding.inflate(inflater,container,false)
        return inflater.inflate(R.layout.fragment_old_event, container, false)
    }


}