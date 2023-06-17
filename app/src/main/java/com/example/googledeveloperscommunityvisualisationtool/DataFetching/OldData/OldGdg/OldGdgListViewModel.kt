package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldGdg

import android.content.Context
import androidx.lifecycle.ViewModel

class OldGdgListViewModel(val oldgdgRepo:OldGdgRepository,val context: Context) : ViewModel() {

    suspend fun getGdgdata(){
        oldgdgRepo.getoldGdgdata()
    }
    fun returnlist():List<oldGdgDataItem>{
        return oldgdgRepo.returnoldGdgItem()
    }
}