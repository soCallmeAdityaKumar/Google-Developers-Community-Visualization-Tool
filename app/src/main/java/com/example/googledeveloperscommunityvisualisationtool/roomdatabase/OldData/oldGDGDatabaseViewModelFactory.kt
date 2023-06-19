package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class oldGDGDatabaseViewModelFactory(val context: Context):ViewModelProvider.Factory {

    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return OldGDGdatabaseViewModel(context) as T
    }
}