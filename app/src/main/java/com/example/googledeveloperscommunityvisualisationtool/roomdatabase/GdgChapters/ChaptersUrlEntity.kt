package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapters

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.Banner

@Entity(tableName = "Chapter_url")
data class ChaptersUrlEntity(
    val avatar: String,
    @Embedded
    val banner: Banner,
    val city: String,
    val city_name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey
    val url: String
    )

