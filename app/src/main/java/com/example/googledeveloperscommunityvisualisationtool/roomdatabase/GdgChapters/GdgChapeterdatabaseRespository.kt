package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import androidx.lifecycle.LiveData
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventEntity

class GdgChapeterdatabaseRespository(val dao:GdgChapterDao) {

    val readAllChapterRepo: LiveData<List<GdgChaptersEntity>> =dao.readAllChapters()

    suspend fun  addChapterRepo(gdgChaptersEntity: GdgChaptersEntity)
    {
        dao.addChapters(gdgChaptersEntity)
    }
}