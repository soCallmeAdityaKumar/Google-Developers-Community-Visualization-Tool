package com.example.googledeveloperscommunityvisualisationtool.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface upcomingEventDao
{

    @Insert
    suspend fun addEvents(upcomingEventEntity: UpcomingEventEntity)

    @Query("SELECT * FROM upcoming_event_entity Order By start_date")
    fun readAllEvent():LiveData<List<UpcomingEventEntity>>

}