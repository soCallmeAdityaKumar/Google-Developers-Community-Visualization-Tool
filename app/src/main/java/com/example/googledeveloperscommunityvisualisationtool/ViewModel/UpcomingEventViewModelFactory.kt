package com.example.googledeveloperscommunityvisualisationtool.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.Repository.UpcomingEventRepository

class UpcomingEventViewModelFactory(val upcomingEventRepository: UpcomingEventRepository,val context:Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcomingEventViewModel(upcomingEventRepository,context) as T
    }
}