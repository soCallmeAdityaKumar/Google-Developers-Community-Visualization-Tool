package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.Banner
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.PastEvents
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.UpcomingEvents

@Entity(tableName = "Chapter_complete_details")
data class ChapterEntity(
    val avatar: String,
    @Embedded
    val banner: Banner,
    val city: String,
    val city_name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val url: String,
    @PrimaryKey
    val gdgName:String,
    val membersNumber:String,
    val about:String,
)
