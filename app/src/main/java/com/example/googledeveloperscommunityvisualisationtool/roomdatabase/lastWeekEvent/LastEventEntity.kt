package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers

@Entity(tableName = "last_week_event_details")
data class LastEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val address:String,
    val gdgName:String,
    val dateAndTime:String,
    val rsvp:String,
    val desc:String,
    val duration:String,

)