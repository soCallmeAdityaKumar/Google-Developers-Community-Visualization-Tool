package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventdatabaseViewModel

class GdgDatabaseViewModelfactory(val context:Context):ViewModelProvider.Factory  {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return GdgChapterDatabaseViewModel(context) as T
    }
}