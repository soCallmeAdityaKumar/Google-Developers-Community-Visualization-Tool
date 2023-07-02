package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.LastWeekDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_week_event")
data class weekEventEntity(
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
    val start_date: String,
    val tags: String,
    val title: String,
    val url: String,
)