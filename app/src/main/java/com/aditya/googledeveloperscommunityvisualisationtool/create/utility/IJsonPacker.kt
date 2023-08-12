package com.aditya.googledeveloperscommunityvisualisationtool.create.utility

import org.json.JSONException
import org.json.JSONObject

interface IJsonPacker<K> {
    @Throws(JSONException::class)
    fun pack(): JSONObject?

    @Throws(JSONException::class)
    fun unpack(obj: JSONObject?): K
}