package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class lastweekroomfactory(val context: Context): ViewModelProvider.Factory{
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return lastweekroommodel(context) as T
    }
}