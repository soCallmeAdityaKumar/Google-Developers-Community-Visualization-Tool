package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.oldGDGroomViewModel

class NotifyViewModelFactory(val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return NotifyViewModel(context) as T
    }
}