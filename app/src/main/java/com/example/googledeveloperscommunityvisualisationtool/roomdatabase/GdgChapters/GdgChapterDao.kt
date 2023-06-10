package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.UpcomingEvents.UpcomingEventEntity

@Dao
interface GdgChapterDao {
    @Insert
    suspend fun addChapters(gdgChaptersEntity: GdgChaptersEntity)

    @Query("SELECT * FROM Chapter_table Order By id")
    fun readAllChapters(): LiveData<List<GdgChaptersEntity>>
}