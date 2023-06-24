package com.example.googledeveloperscommunityvisualisationtool.Fragments.GDGChapterDetails

import com.example.googledeveloperscommunityvisualisationtool.create.utility.IJsonPacker
import org.json.JSONException
import org.json.JSONObject

class GDG : InfoScraping, IJsonPacker<Any?> {
    var id: Long = 0
        private set
    var urlName: String? = null
        private set
    var status: String? = null
        private set
    var city: String? = null
        private set
    var country: String? = null
        private set
    var name: String? = null
        private set
    var longitude = 0.0
        private set
    var latitude = 0.0
        private set

    constructor() : super(Constant.GDG.id) {}
    constructor(
        id: Long,
        urlName: String?,
        status: String?,
        city: String?,
        country: String?,
        name: String?,
        longitude: Double,
        latitude: Double
    ) : super(
        Constant.GDG.id
    ) {
        this.id = id
        this.urlName = urlName
        this.status = status
        this.city = city
        this.country = country
        this.name = name
        this.longitude = longitude
        this.latitude = latitude
    }

    @Throws(JSONException::class)
    override fun pack(): JSONObject? {
        val obj = JSONObject()
        obj.put("type", Constant.GDG.id)
        obj.put("id", id)
        obj.put("urlname", urlName)
        obj.put("status", status)
        obj.put("city", city)
        obj.put("country", country)
        obj.put("name", name)
        obj.put("lon", longitude)
        obj.put("lat", latitude)
        return obj
    }

    @Throws(JSONException::class)
    override fun unpack(obj: JSONObject?): GDG {
        type = Constant.GDG.id
        id = obj!!.getLong("id")
        urlName = obj.getString("urlname")
        status = obj.getString("status")
        city = obj.getString("city")
        country = obj.getString("country")
        name = obj.getString("name")
        longitude = obj.getDouble("lon")
        latitude = obj.getDouble("lat")
        return this
    }
}