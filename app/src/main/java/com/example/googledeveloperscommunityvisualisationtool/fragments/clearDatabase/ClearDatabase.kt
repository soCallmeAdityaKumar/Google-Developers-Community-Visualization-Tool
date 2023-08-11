package com.example.googledeveloperscommunityvisualisationtool.fragments.clearDatabase

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentClearDatabaseBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapViewModelFact
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroommodel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters.ChapUrlroomfactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase.lastweekroomfactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventModelFact
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent.LastEventViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModelFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventRoomFactory
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel

class ClearDatabase : Fragment() {
    lateinit var binding:FragmentClearDatabaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentClearDatabaseBinding.inflate(inflater,container,false)
        val view=binding.root



        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }




}