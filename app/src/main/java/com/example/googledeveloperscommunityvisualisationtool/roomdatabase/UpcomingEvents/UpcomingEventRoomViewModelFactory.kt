package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpcomingEventRoomViewModelFactory(val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcomingEventdatabaseViewModel(context) as T
    }
}