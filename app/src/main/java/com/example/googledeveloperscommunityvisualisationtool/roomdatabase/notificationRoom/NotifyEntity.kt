package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notification_table")
data class NotifyEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val desc:String,
    val timing:Long,
    val image:String
    )