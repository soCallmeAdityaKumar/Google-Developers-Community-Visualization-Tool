package com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GdgChaptersViewModelFactory(val gdgChaptersRepo:GdgScrapingRespository,val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return GdgViewModel(gdgChaptersRepo,context) as T
    }
}