package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldGdg

import android.content.Context
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

const val  url="https://gdgsearch.com/src/list.json"
class OldGdgRepository(val context:Context) {
    private var oldgdglist:MutableList<oldGdgDataItem> = mutableListOf()
    fun getoldGdgdata(){
        val list= mutableListOf<oldGdgDataItem>()

        var queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, url, { response ->

            val res = JSONArray(response)
            for(i in 0 until res.length()){
                val city=res.getJSONObject(i).getString("city")
                val country=res.getJSONObject(i).getString("country")
                val id=res.getJSONObject(i).getInt("id")
                val latitude=res.getJSONObject(i).optDouble("lat",0.0)
                val longitude=res.getJSONObject(i).optDouble("lon",0.0)
                val name=res.getJSONObject(i).getString("name")
                val state=res.getJSONObject(i).optString("state","")
                val status=res.getJSONObject(i).getString("status")
                val urlname=res.getJSONObject(i).getString("urlname")

                val item=oldGdgDataItem(city,country,id,latitude,longitude,name,state,status,urlname)
                Log.d("item",item.toString())
                oldgdglist.add(item)
            }

        })
        {
            Log.d("OldGdgDataVolleyError", it.toString())
        }
        queue.add(stringRequest)

    }
    fun returnoldGdgItem():List<oldGdgDataItem>{
        return oldgdglist
    }
}