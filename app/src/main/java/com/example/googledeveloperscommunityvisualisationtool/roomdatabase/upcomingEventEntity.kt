package com.example.googledeveloperscommunityvisualisationtool.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Chapter
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result

@Entity(tableName = "upcoming_event_entity")
data class upcomingEventEntity
    (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val chapter:Chapter,
    val city:String,
    val description:String,
    val eventTitle:String,
    val eventId:Int,
    @ColumnInfo(name = "Start_Date")val startDate:String,
    val tags:List<String>,
    val title:String,
    val url:String
    )