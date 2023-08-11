package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails

import androidx.lifecycle.LiveData

class ChapterRespository(val dao: ChapterDao) {
    val readChapterRepo: LiveData<List<ChapterEntity>> =dao.readChapter()

    suspend fun  addChaptersRepo(chapterEntity: ChapterEntity)
    {
        dao.addChapters(chapterEntity)
    }

    suspend fun deleteAllChapter(){
        dao.deleteAllGDGChapter()
    }

}