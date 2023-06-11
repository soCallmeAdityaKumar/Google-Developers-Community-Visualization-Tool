package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import androidx.lifecycle.LiveData

class ChapterUrldatabaseRespository(val dao:GdgChapterUrlDao) {

    val readChapterurlRepo: LiveData<List<ChaptersUrlEntity>> =dao.readAllChaptersurl()

    suspend fun  addChapterUrlRepo(chaptersUrlEntity: ChaptersUrlEntity)
    {
        dao.addChaptersUrl(chaptersUrlEntity)
    }


}