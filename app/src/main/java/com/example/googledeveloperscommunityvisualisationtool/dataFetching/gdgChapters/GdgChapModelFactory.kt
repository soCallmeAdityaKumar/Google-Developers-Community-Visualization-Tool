package com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GdgChapModelFactory(val gdgChaptersRepo:GdgScrapingRespo, val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return GdgViewModel(gdgChaptersRepo,context) as T
    }
}