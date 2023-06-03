package com.example.googledeveloperscommunityvisualisationtool.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.Repository.UpcomingEventRepository

class UpcomingEventViewModelFactory(val upcomingEventRepository: UpcomingEventRepository):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcomingEventViewModel(upcomingEventRepository) as T
    }
}