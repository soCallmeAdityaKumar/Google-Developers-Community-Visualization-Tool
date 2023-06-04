package com.example.googledeveloperscommunityvisualisationtool.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result


@Dao
interface upcomingEventDao
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEvents(event:Result)

    @Query("SELECT * FROM upcoming_event_entity Order By Start_Date")
    fun readAllEvent():LiveData<List<Result>>

}