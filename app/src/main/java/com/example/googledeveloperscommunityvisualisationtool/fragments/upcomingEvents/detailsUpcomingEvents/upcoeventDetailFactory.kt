package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.upcomingEvents.UpcoEventViewMod
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomViewModel

class upcoeventDetailFactory(val repo: upcoEventsDetailsRepo) :ViewModelProvider.Factory{
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return UpcoEventDetailsModel(repo) as T
    }
}