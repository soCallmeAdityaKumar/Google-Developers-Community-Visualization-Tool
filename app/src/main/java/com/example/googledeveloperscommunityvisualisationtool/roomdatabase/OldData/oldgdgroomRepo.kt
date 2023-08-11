package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import androidx.lifecycle.LiveData

class oldgdgroomRepo(val dao:OldGDGdao) {
    val readoldChaptersRepo: LiveData<List<OldGDGEntity>> =dao.readAlloldGDG()

    suspend fun  addoldgdgRepo(oldGDGEntity: OldGDGEntity)
    {
        dao.addoldChapters(oldGDGEntity)
    }
    suspend fun deleteAllOldGDG(){
        dao.deleteallOldData()
    }
}