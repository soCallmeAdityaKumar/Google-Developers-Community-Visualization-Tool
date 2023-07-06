package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.upcomingEvents

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.googledeveloperscommunityvisualisationtool.dataClass.volley.EventTypeLogo
import com.example.googledeveloperscommunityvisualisationtool.dataClass.volley.Picture

@Entity(tableName = "upcoming_event_entity")
data class UpcomingEventEntity
    (
    val Chapter_location: String,
    val CityName: String,
    val Country: String,
    val Country_name: String,
    val Description: String,
    val ChapterId: Int,
    val Relative_url: String,
    val State: String,
    val Timezone: String,
    val ChapterTitle: String,
    val ChapterUrl: String,
    val city: String,
    val description_short: String,
    val event_type_title: String,
    @PrimaryKey
    val id: Int,
    @Embedded
    val picture: Picture,
    val start_date: String,
    val tags: String,
    val title: String,
    val url: String,
    )

