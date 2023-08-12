package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl

class NotifyViewModelFactory(val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return NotifyViewModel(context) as T
    }
}