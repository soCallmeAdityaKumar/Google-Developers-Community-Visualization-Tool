package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcoEventViewMod
import com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomViewModel

class upcoeventDetailFactory(val repo: upcoEventsDetailsRepo) :ViewModelProvider.Factory{
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcoEventDetailsModel(repo) as T
    }
}