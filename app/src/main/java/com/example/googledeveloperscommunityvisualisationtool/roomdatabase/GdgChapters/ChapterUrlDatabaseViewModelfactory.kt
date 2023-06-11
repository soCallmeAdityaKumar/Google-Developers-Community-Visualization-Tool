package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChapterUrlDatabaseViewModelfactory(val context:Context):ViewModelProvider.Factory  {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return ChapterUrlDatabaseViewModel(context) as T
    }
}