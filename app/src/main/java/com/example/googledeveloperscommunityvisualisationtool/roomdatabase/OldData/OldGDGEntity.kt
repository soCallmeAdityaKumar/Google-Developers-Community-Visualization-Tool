package com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "old_gdg_group")
data class OldGDGEntity(
    val city: String,
    val country: String,
    @PrimaryKey
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

    companion object CREATOR : Parcelable.Creator<OldGDGEntity> {
        override fun createFromParcel(parcel: Parcel): OldGDGEntity {
            return OldGDGEntity(parcel)
        }

        override fun newArray(size: Int): Array<OldGDGEntity?> {
            return arrayOfNulls(size)
        }
    }
}