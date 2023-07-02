package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel

class lastweekroomfactory(val context: Context): ViewModelProvider.Factory{
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return lastweekroommodel(context) as T
    }
}