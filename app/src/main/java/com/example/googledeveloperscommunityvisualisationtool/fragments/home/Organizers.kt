package com.example.googledeveloperscommunityvisualisationtool.fragments.home

import android.os.Parcel
import android.os.Parcelable

data class Organizers(
    val organizername:String,
    val organizercompany:String,
    var organizerTitle:String,
    val organizerimage:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(organizername)
        parcel.writeString(organizercompany)
        parcel.writeString(organizerTitle)
        parcel.writeString(organizerimage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Organizers> {
        override fun createFromParcel(parcel: Parcel): Organizers {
            return Organizers(parcel)
        }

        override fun newArray(size: Int): Array<Organizers?> {
            return arrayOfNulls(size)
        }
    }
}