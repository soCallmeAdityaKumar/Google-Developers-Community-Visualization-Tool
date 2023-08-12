package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChapters(chapterEntity: ChapterEntity)

    @Query("SELECT * FROM Chapter_complete_details")
    fun readChapter(): LiveData<List<ChapterEntity>>

    @Query("DELETE FROM Chapter_complete_details")
    suspend fun deleteAllGDGChapter()
}