package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents.UpcoEventroomViewmodel

class UpcoEventDetFactory(val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcoEventDetViewModel(context) as T
    }
}