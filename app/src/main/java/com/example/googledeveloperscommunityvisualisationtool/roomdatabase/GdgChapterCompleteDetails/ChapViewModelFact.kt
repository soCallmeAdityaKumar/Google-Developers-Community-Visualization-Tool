package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChapViewModelFact(val context: Context):ViewModelProvider.Factory {
        override fun<T: ViewModel>create(modelClass:Class<T>):T{
            return ChapterViewModel(context) as T
        }
    }
