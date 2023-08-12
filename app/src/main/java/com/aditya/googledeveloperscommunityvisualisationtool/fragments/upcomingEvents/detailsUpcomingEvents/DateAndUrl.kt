package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField


data class DateAndUrl(val url:String,val dateAndTime:String,val logo:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(dateAndTime)
        parcel.writeString(logo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateAndUrl> {
        override fun createFromParcel(parcel: Parcel): DateAndUrl {
            return DateAndUrl(parcel)
        }

        override fun newArray(size: Int): Array<DateAndUrl?> {
            return arrayOfNulls(size)
        }
    }

}