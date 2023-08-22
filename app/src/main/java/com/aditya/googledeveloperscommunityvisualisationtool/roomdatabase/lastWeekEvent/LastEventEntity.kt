package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.lastWeekEvent

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers

@Entity(tableName = "last_week_event_details")
data class LastEventEntity(
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