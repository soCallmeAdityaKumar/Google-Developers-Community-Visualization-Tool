package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import androidx.lifecycle.LiveData
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters.ChaptersUrlEntity

class oldgdgdatabaseRepository(val dao:OldGDGdao) {
    val readoldChaptersRepo: LiveData<List<OldGDGEntity>> =dao.readAlloldGDG()

    suspend fun  addoldgdgRepo(oldGDGEntity: OldGDGEntity)
    {
        dao.addoldChapters(oldGDGEntity)
    }
}