package com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpcomingEventfactory(val upcomEventRepo: UpcomEventRepo, val context:Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcoEventViewMod(upcomEventRepo,context) as T
    }
}