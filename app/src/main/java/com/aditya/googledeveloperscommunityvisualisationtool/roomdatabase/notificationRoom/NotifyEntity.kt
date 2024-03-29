package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notification_table")
data class NotifyEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val desc:String,
    val timeinMiliSec:Long,
    val image:String,
    val time:String
    )