package com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.Movement

import android.os.Parcel
import android.os.Parcelable
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.IJsonPacker
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.Action
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionIdentifier
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import org.json.JSONException
import org.json.JSONObject

class Movement : Action, IJsonPacker<Any?>, Parcelable {
    var poi: POI? = null
        private set
    var newHeading = 0.0
        private set
    var newTilt = 0.0
        private set
    var isOrbitMode = false
        private set
    var duration = 0
        private set

    /**
     * Empty Constructor
     */
    constructor() : super(ActionIdentifier.MOVEMENT_ACTIVITY.id) {}
    constructor(
        id: Long,
        poi: POI?,
        newHeading: Double,
        newTilt: Double,
        isOrbitMode: Boolean,
        duration: Int
    ) : super(id, ActionIdentifier.MOVEMENT_ACTIVITY.id) {
        this.poi = poi
        this.newHeading = newHeading
        this.newTilt = newTilt
        this.isOrbitMode = isOrbitMode
        this.duration = duration
    }

    constructor(`in`: Parcel) : super(`in`.readLong(), ActionIdentifier.MOVEMENT_ACTIVITY.id) {
        poi = `in`.readParcelable(POI::class.java.classLoader)
        newHeading = `in`.readDouble()
        newTilt = `in`.readDouble()
        isOrbitMode = `in`.readInt() != 0
        duration = `in`.readInt()
    }

    constructor(movement: Movement) : super(movement.id, ActionIdentifier.MOVEMENT_ACTIVITY.id) {
        poi = movement.poi
        newHeading = movement.newHeading
        newTilt = movement.newTilt
        isOrbitMode = movement.isOrbitMode
        duration = movement.duration
    }

    fun setPoi(poi: POI?): Movement {
        this.poi = poi
        return this
    }

    fun setNewHeading(newHeading: Double): Movement {
        this.newHeading = newHeading
        return this
    }

    fun setNewTilt(newTilt: Double): Movement {
        this.newTilt = newTilt
        return this
    }

    fun setOrbitMode(orbitMode: Boolean): Movement {
        isOrbitMode = orbitMode
        return this
    }

    fun setDuration(duration: Int): Movement {
        this.duration = duration
        return this
    }

    @Throws(JSONException::class)
    override fun pack(): JSONObject? {
        val obj = JSONObject()
        obj.put("movement_id", id)
        obj.put("type", type)
        if (poi != null) obj.put("movement_poi", poi!!.pack())
        obj.put("movement_new_heading", newHeading)
        obj.put("movement_new_tilt", newTilt)
        obj.put("movement_orbit_mode", isOrbitMode)
        obj.put("movement_duration", duration)
        return obj
    }

    @Throws(JSONException::class)
    override fun unpack(obj: JSONObject?): Movement {
        id = obj!!.getLong("movement_id")
        type = obj.getInt("type")
        val newPoi = POI()
        poi = try {
            newPoi.unpack(obj.getJSONObject("movement_poi"))
        } catch (JSONException: JSONException) {
            null
        }
        newHeading = obj.getDouble("movement_new_heading")
        newTilt = obj.getDouble("movement_new_tilt")
        isOrbitMode = obj.getBoolean("movement_orbit_mode")
        duration = obj.getInt("movement_duration")
        return this
    }

    override fun toString(): String {
        return "Location Name: " + poi!!.poiLocation?.name + " New Heading: " + newHeading +
                " New Tilt: " + newTilt + " Duration: " + duration
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(poi, flags)
        parcel.writeDouble(newHeading)
        parcel.writeDouble(newTilt)
        parcel.writeInt(if (isOrbitMode) 1 else 0)
        parcel.writeInt(duration)
    }



    companion object CREATOR : Parcelable.Creator<Movement> {
        override fun createFromParcel(parcel: Parcel): Movement {
            return Movement(parcel)
        }

        override fun newArray(size: Int): Array<Movement?> {
            return arrayOfNulls(size)
        }
    }
}