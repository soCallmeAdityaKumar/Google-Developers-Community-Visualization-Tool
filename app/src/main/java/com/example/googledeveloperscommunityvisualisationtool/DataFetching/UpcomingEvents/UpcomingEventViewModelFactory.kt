package com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpcomingEventViewModelFactory(val upcomingEventRepository: UpcomingEventRepository, val context:Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcomingEventViewModel(upcomingEventRepository,context) as T
    }
}