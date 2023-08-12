package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

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