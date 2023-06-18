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
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeInt(id)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
        parcel.writeString(name)
        parcel.writeString(state)
        parcel.writeString(status)
        parcel.writeString(urlname)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<oldGdgDataItem> {
        override fun createFromParcel(parcel: Parcel): oldGdgDataItem {
            return oldGdgDataItem(parcel)
        }

        override fun newArray(size: Int): Array<oldGdgDataItem?> {
            return arrayOfNulls(size)
        }
    }
}
