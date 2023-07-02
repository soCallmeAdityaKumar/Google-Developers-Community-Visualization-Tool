package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface lastWeekDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addlastweekEvents(weekEventEntity: weekEventEntity)

    @Query("SELECT * FROM last_week_event Order By start_date")
    fun readlastweekEvent(): LiveData<List<weekEventEntity>>
}