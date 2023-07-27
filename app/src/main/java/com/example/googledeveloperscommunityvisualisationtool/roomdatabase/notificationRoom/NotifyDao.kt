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

    @Query("SELECT * FROM notification_table")
    fun readNotification(): LiveData<List<NotifyEntity>>
    @Query("DELETE FROM notification_table")
    suspend fun deleteAllNotification()
}