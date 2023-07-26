package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotifyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNotification(notifyEntity: NotifyEntity)

    @Query("SELECT * FROM notification_table Order By timing")
    fun readNotification(): LiveData<List<NotifyEntity>>
}