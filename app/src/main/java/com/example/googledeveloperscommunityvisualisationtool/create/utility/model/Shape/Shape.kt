package com.example.googledeveloperscommunityvisualisationtool.create.utility.model.Shape

import android.os.Parcel
import android.os.Parcelable
import com.example.googledeveloperscommunityvisualisationtool.create.utility.IJsonPacker
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.Action
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.ActionIdentifier
import com.example.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Shape : Action, IJsonPacker<Shape>, Parcelable {
    var poi: POI?
        private set
    var points: List<*>?
        private set
    var isExtrude: Boolean
        private set
    var duration: Int
        private set

    constructor() : super(ActionIdentifier.SHAPES_ACTIVITY.id) {
        poi = null
        points = ArrayList<Any>()
        isExtrude = false
        duration = 0
    }

    constructor(
        id: Long,
        points: List<Point?>?,
        isExtrude: Boolean,
        poi: POI?,
        duration: Int
    ) : super(id, ActionIdentifier.SHAPES_ACTIVITY.id) {
        this.poi = poi
        this.points = points
        this.isExtrude = isExtrude
        this.duration = duration
    }

    constructor(`in`: Parcel) : super(`in`.readLong(), ActionIdentifier.SHAPES_ACTIVITY.id) {
        poi = `in`.readParcelable(POI::class.java.classLoader)
        points = `in`.readArrayList(Point::class.java.classLoader)
        isExtrude = `in`.readInt() != 0
        duration = `in`.readInt()
    }

    constructor(shape: Shape) : super(shape.id, ActionIdentifier.SHAPES_ACTIVITY.id) {
        poi = shape.poi
        points = shape.points
        isExtrude = shape.isExtrude
        duration = shape.duration
    }

    fun setPoints(points: List<Point?>?): Shape {
        this.points = points
        return this
    }

    fun setExtrude(extrude: Boolean): Shape {
        isExtrude = extrude
        return this
    }

    fun setPoi(poi: POI?): Shape {
        this.poi = poi
        return this
    }

    fun setDuration(duration: Int): Shape {
        this.duration = duration
        return this
    }

    @Throws(JSONException::class)
    override fun pack(): JSONObject? {
        val obj = JSONObject()
        obj.put("shape_id", id)
        obj.put("type", type)
        if (poi != null) obj.put("shape_poi", poi!!.pack())
        val jsonPoints = JSONArray()
        for (i in points!!.indices) {
            jsonPoints.put((points!![i] as Point).pack())
        }
        obj.put("jsonPoints", jsonPoints)
        obj.put("isExtrude", isExtrude)
        obj.put("duration", duration)
        return obj
    }

    @Throws(JSONException::class)
    override fun unpack(obj: JSONObject?): Shape {
        id = obj!!.getLong("shape_id")
        type = obj.getInt("type")
        val newPoi = POI()
        poi = try {
            newPoi.unpack(obj.getJSONObject("shape_poi"))
        } catch (JSONException: JSONException) {
            null
        }
        val jsonPoints = obj.getJSONArray("jsonPoints")
        val arrayPoint: MutableList<Point> = ArrayList()
        for (i in 0 until jsonPoints.length()) {
            val point = Point()
            point.unpack(jsonPoints.getJSONObject(i))
            arrayPoint.add(point)
        }
        points = arrayPoint
        isExtrude = obj.getBoolean("isExtrude")
        duration = obj.getInt("duration")
        return this
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Location Name: ").append(poi!!.poiLocation?.name)
        for (i in points!!.indices) {
            stringBuilder.append("Point ").append(i).append(": ").append(points!![i].toString())
                .append("\n")
        }
        return stringBuilder.toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(poi, flags)
        parcel.writeList(points)
        parcel.writeInt(if (isExtrude) 1 else 0)
        parcel.writeInt(duration)
    }

    companion object CREATOR : Parcelable.Creator<Shape> {
        override fun createFromParcel(parcel: Parcel): Shape {
            return Shape(parcel)
        }

        override fun newArray(size: Int): Array<Shape?> {
            return arrayOfNulls(size)
        }
    }
}