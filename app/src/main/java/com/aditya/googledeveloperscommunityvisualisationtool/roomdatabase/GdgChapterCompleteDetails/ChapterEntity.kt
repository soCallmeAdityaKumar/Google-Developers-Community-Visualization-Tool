package com.aditya.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aditya.googledeveloperscommunityvisualisationtool.dataClass.gdgGroupClasses.Banner


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
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        TODO("banner"),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatar)
        parcel.writeString(city)
        parcel.writeString(city_name)
        parcel.writeString(country)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(url)
        parcel.writeString(gdgName)
        parcel.writeString(membersNumber)
        parcel.writeString(about)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChapterEntity> {
        override fun createFromParcel(parcel: Parcel): ChapterEntity {
            return ChapterEntity(parcel)
        }

        override fun newArray(size: Int): Array<ChapterEntity?> {
            return arrayOfNulls(size)
        }
    }
}