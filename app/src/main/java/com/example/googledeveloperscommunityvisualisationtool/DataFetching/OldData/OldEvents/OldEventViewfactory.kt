package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters.GdgViewModel

class OldEventViewfactory(val repository: OldEventRepository,val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return OldEventViewModel(repository,context) as T
    }
}