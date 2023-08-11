package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.gdgChapters

import androidx.lifecycle.LiveData

class ChapUrlroomRespo(val dao: GdgChapterUrlDao) {

    val readChapterurlRepo: LiveData<List<ChaptersUrlEntity>> =dao.readAllChaptersurl()

    suspend fun  addChapterUrlRepo(chaptersUrlEntity: ChaptersUrlEntity)
    {
        dao.addChaptersUrl(chaptersUrlEntity)
    }

    suspend fun deleteChapterUrl(){
        dao.deleteAlChapterUrl()
    }

}