package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class oldEventViewfactory(val repository: oldEventRepository, val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return OldEventViewModel(repository,context) as T
    }
}