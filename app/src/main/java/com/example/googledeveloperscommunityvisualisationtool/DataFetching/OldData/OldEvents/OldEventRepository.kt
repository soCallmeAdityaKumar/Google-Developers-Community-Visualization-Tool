package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents.OldEventsDataClass.oldEventsData
import com.example.googledeveloperscommunityvisualisationtool.DataFetching.UpcomingEvents.url
import org.json.JSONArray
import org.json.JSONObject

const val baseurl="https://github.com/kevinsimper/gdg-events/tree/bfa3df84b0b444788a3e794a27ec4ef91a888b4e/"
class OldEventRepository (val context:Context){
 var oldEventsData: MutableList<oldEventsData> = mutableListOf()
    fun getResponse(endpoint:String){
        var queue = Volley.newRequestQueue(context)
        val url= baseurl+endpoint
        val stringRequest = StringRequest(Request.Method.GET,url, { response ->

            val result = JSONArray(response)
            for( i in 0 until result.length()  ){
                val events=result.getJSONObject(i).getJSONArray("Event")
                Log.d("oldevent",events.toString())
            }


        }){
            Log.d("OldEventsRespository","Volley error: $it")
        }

        queue.add(stringRequest)

    }
}