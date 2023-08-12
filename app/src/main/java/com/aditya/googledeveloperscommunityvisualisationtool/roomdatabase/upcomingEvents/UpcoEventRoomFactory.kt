package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpcoEventRoomFactory(val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcoEventroomViewmodel(context) as T
    }
}