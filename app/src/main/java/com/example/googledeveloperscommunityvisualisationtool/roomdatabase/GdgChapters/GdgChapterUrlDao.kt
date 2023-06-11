package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GdgChapterUrlDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChaptersUrl(chaptersUrlEntity: ChaptersUrlEntity)

    @Query("SELECT * FROM Chapter_url")
    fun readAllChaptersurl(): LiveData<List<ChaptersUrlEntity>>



}