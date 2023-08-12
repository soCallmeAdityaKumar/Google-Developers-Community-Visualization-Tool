package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LastEventModelFact(val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return LastEventViewModel(context) as T
    }
}