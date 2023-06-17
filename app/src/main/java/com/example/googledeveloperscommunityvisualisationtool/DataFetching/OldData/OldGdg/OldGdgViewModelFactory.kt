package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldGdg

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventdatabaseViewModel

class OldGdgViewModelFactory(val repo:OldGdgRepository,val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return OldGdgListViewModel(repo,context) as T
    }
}