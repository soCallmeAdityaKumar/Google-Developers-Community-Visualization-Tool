package com.example.googledeveloperscommunityvisualisationtool.create.utility.model.poi

import android.os.Parcel
import android.os.Parcelable
import com.example.googledeveloperscommunityvisualisationtool.create.utility.IJsonPacker
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.Action
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionIdentifier
import org.json.JSONException
import org.json.JSONObject

class POI : Action, IJsonPacker<Any?>, Parcelable {
    var poiLocation: POILocation? = null
        private set
    var poiCamera: POICamera? = null
        private set

    /**
     * Empty Constructor
     */
    constructor() : super(ActionIdentifier.LOCATION_ACTIVITY.id) {}
    constructor(id: Long, poiLocation: POILocation?, poiCamera: POICamera?) : super(
        id,
        ActionIdentifier.LOCATION_ACTIVITY.id
    ) {
        this.poiLocation = poiLocation
        this.poiCamera = poiCamera
    }

    constructor(`in`: Parcel) : super(`in`.readLong(), ActionIdentifier.LOCATION_ACTIVITY.id) {
        val name = `in`.readString()
        val longitude = `in`.readDouble()
        val latitude = `in`.readDouble()
        val altitude = `in`.readDouble()
        poiLocation = POILocation(name!!, longitude, latitude, altitude)
        val heading = `in`.readDouble()
        val tilt = `in`.readDouble()
        val range = `in`.readDouble()
        val altitudeMode = `in`.readString()
        val duration = `in`.readInt()
        poiCamera = POICamera(heading, tilt, range, altitudeMode!!, duration)
    }

    constructor(poi: POI) : super(poi.id, ActionIdentifier.LOCATION_ACTIVITY.id) {
        poiLocation = poi.poiLocation
        poiCamera = poi.poiCamera
    }

    fun setPoiLocation(poiLocation: POILocation?): POI {
        this.poiLocation = poiLocation
        return this
    }

    fun setPoiCamera(poiCamera: POICamera?): POI {
        this.poiCamera = poiCamera
        return this
    }

    @Throws(JSONException::class)
    override fun pack(): JSONObject? {
        val obj = JSONObject()
        obj.put("poi_id", id)
        obj.put("type", type)
        obj.put("poi_location_name", poiLocation!!.name)
        obj.put("poi_location_longitude", poiLocation!!.longitude)
        obj.put("poi_location_latitude", poiLocation!!.latitude)
        obj.put("poi_camera_altitude", poiLocation!!.altitude)
        obj.put("poi_camera_heading", poiCamera!!.heading)
        obj.put("poi_camera_tilt", poiCamera!!.tilt)
        obj.put("poi_camera_range", poiCamera!!.range)
        obj.put("poi_camera_altitudeMode", poiCamera!!.altitudeMode)
        obj.put("poi_camera_duration", poiCamera!!.duration)
        return obj
    }

    @Throws(JSONException::class)
    override fun unpack(obj: JSONObject?): POI {
        id = obj!!.getLong("poi_id")
        type = obj.getInt("type")
        val name = obj.getString("poi_location_name")
        val longitude = obj.getDouble("poi_location_longitude")
        val latitude = obj.getDouble("poi_location_latitude")
        val altitude = obj.getDouble("poi_camera_altitude")
        poiLocation = POILocation(name, longitude, latitude, altitude)
        val heading = obj.getDouble("poi_camera_heading")
        val tilt = obj.getDouble("poi_camera_tilt")
        val range = obj.getDouble("poi_camera_range")
        val altitudeMode = obj.getString("poi_camera_altitudeMode")
        val duration = obj.getInt("poi_camera_duration")
        poiCamera = POICamera(heading, tilt, range, altitudeMode, duration)
        return this
    }

    override fun toString(): String {
        return poiLocation!!.name
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(poiLocation!!.name)
        parcel.writeDouble(poiLocation!!.longitude)
        parcel.writeDouble(poiLocation!!.latitude)
        parcel.writeDouble(poiLocation!!.altitude)
        parcel.writeDouble(poiCamera!!.heading)
        parcel.writeDouble(poiCamera!!.tilt)
        parcel.writeDouble(poiCamera!!.range)
        parcel.writeString(poiCamera!!.altitudeMode)
        parcel.writeInt(poiCamera!!.duration)
    }



    companion object CREATOR : Parcelable.Creator<POI> {
        override fun createFromParcel(parcel: Parcel): POI {
            return POI(parcel)
        }

        override fun newArray(size: Int): Array<POI?> {
            return arrayOfNulls(size)
        }
    }


}