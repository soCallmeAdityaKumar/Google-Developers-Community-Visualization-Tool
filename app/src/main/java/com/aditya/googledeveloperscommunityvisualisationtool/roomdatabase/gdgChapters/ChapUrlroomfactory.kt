package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChapUrlroomfactory(val context:Context):ViewModelProvider.Factory  {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return ChapUrlroomViewModel(context) as T
    }
}