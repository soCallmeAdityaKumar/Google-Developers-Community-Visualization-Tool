package com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldGdg

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OldGdgViewModelFactory(val repo:OldGdgRepository,val context: Context):ViewModelProvider.Factory {
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return OldGdgListViewModel(repo,context) as T
    }
}