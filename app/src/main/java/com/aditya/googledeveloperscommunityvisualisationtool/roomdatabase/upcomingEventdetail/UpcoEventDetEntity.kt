package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEventdetail

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upco_event_detials")
data class UpcoEventDetEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val eventName:String,
    val addresss:String,
    val url:String,
    val rsvp:String,
    val gdgName:String,
    val date:String,
    val aboutEvent:String,
    val organizers:String
)