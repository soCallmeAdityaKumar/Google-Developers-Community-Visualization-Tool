package com.example.googledeveloperscommunityvisualisationtool.create.utility.model.Shape

import android.os.Parcel
import android.os.Parcelable
import com.example.googledeveloperscommunityvisualisationtool.create.utility.IJsonPacker
import org.json.JSONException
import org.json.JSONObject

class Point : IJsonPacker<Any?>, Parcelable {
    var id: Long = 0
    var longitude = 0.0
    var latitude = 0.0
    var altitude = 0.0

    constructor() {}
    constructor(id: Long, longitude: Double, latitude: Double, altitude: Double) {
        this.id = id
        this.longitude = longitude
        this.latitude = latitude
        this.altitude = altitude
    }

    constructor(`in`: Parcel) {
        id = `in`.readLong()
        longitude = `in`.readDouble()
        latitude = `in`.readDouble()
        altitude = `in`.readDouble()
    }

    @Throws(JSONException::class)
    override fun pack(): JSONObject? {
        val obj = JSONObject()
        obj.put("point_id", id)
        obj.put("longitude", longitude)
        obj.put("latitude", latitude)
        obj.put("altitude", altitude)
        return obj
    }

    @Throws(JSONException::class)
    override fun unpack(obj: JSONObject?): Point {
        id = obj!!.getLong("point_id")
        longitude = obj.getDouble("longitude")
        latitude = obj.getDouble("latitude")
        altitude = obj.getDouble("altitude")
        return this
    }

    override fun toString(): String {
        return "Longitude: $longitude Latitude: $latitude Altitude: $altitude"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
        parcel.writeDouble(altitude)
    }



    companion object CREATOR : Parcelable.Creator<Point> {
        override fun createFromParcel(parcel: Parcel): Point {
            return Point(parcel)
        }

        override fun newArray(size: Int): Array<Point?> {
            return arrayOfNulls(size)
        }
    }

}