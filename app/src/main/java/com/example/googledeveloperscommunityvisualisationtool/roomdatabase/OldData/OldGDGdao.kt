package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OldGDGdao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addoldChapters(oldChapter: OldGDGEntity)

    @Query("SELECT * FROM old_gdg_group")
    fun readAlloldGDG(): LiveData<List<OldGDGEntity>>
}