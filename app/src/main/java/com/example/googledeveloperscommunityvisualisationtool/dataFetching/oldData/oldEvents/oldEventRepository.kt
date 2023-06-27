package com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Event
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Group
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.GroupProfile
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Photo
import com.example.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.oldEventsData
import org.json.JSONObject

class oldEventRepository (val context:Context){
    val baseurl="https://raw.githubusercontent.com/kevinsimper/gdg-events/master/"
    var event=oldEventsData(
        listOf(Event("",Group("","","",""),"",""
            ,"","")), listOf(Organizer("",GroupProfile("",""),"","",Photo(""),""))
    )
    fun getResponse(endpoint:String){
        var queue = Volley.newRequestQueue(context)
        val url= baseurl+endpoint+".json"
        val stringRequest = StringRequest(Request.Method.GET,url, { response ->
            val result = JSONObject(response)
                val events=result.getJSONArray("events")
                val eventlist= mutableListOf<Event>()
                for( j in 0 until events.length()){
                    val desc=events.getJSONObject(j).optString("description","")
                    val group=events.getJSONObject(j).getJSONObject("group")
                    val link=events.getJSONObject(j).optString("link","")
                    val country=group.getString("region")
                    val localizedLocation=group.optString("localized_location","")
                    val state=group.optString("state","")
                    val groupName=group.optString("name","")
                    val eventName=events.getJSONObject(j).optString("name","")
                    val date=events.getJSONObject(j).optString("local_date","")
                    val time=events.getJSONObject(j).optString("local_time","")
                    Log.d("oldeventdata"," events->${Event(desc,Group(country,localizedLocation,groupName,state),link,date,time,eventName)}")
                    eventlist.add(Event(desc,Group(country,localizedLocation,groupName,state),link,date,time,eventName))
                }
                val organizersList= mutableListOf<Organizer>()
                val organizer=result.getJSONArray("organizers")
                for(j in 0 until organizer.length()){
//                    val bio=organizer.getJSONObject(j).getString("bio")
                    val country=organizer.getJSONObject(j).getString("country")
                    val groupProfile=organizer.getJSONObject(j).getJSONObject("group_profile")
                    val groupProfileLink=groupProfile.getString("link")
                    val groupProfileRole=groupProfile.getString("role")
                    val localisedCountry=organizer.getJSONObject(j).getString("localized_country_name")
                    val name=organizer.getJSONObject(j).getString("name")
                    val photo=organizer.getJSONObject(j).getJSONObject("photo")
                    val baseurl=photo.getString("photo_link")
                    val state=organizer.getJSONObject(j).optString("state","")

                    Log.d("oldeventdata"," organizers->${Organizer(country, GroupProfile(groupProfileLink,groupProfileRole),localisedCountry,name,
                        Photo(baseurl),state)}")
                    organizersList.add(Organizer(country, GroupProfile(groupProfileLink,groupProfileRole),localisedCountry,name,
                        Photo(baseurl),state))

                }
                 event=oldEventsData(eventlist,organizersList)


        }){
            Log.d("OldEventsRespository","Volley error: $it")
        }

        queue.add(stringRequest)

    }
}