package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldGdg

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize

data class oldGdgDataItem(
    val city: String,
    val country: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val state: String,
    val status: String,
    val urlname: String
)
