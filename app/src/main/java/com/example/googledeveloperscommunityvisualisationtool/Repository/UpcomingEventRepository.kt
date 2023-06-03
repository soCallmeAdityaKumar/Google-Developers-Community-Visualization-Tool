package com.example.googledeveloperscommunityvisualisationtool.Repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Chapter
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Logo
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Picture
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Objects
import kotlin.math.log

const val  url = "https://gdg.community.dev/api/search/?result_types=upcoming_event&order_by_proximity=true&proximity=3300"

class UpcomingEventRepository(val context: Context) {
     var resultArray= mutableListOf<Result>()

    fun getResponse() :MutableList<Result>{
            var queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, url, {response->
                val res=JSONObject(response)
//                val location=res.getString("location")
                val results=res.getJSONArray("results")
                Log.d("id",results.toString())
                Log.d("length",results.length().toString())
                for( i in 0 until results.length()){
                    val result=results.getJSONObject(i)//returns each gdg group upcoming events details

                    //for chapters
                    val chapterLocation=result.getJSONObject("chapter").getString("chapter_location")
                    val chapterCity=result.getJSONObject("chapter").getString("city")
                    val chapterCountry=result.getJSONObject("chapter").getString("country")
                    val chapterCountryName=result.getJSONObject("chapter").getString("country_name")
                    val chapterDescription=result.getJSONObject("chapter").getString("description")
                    val chapterId=result.getJSONObject("chapter").getInt("id")

                    //for logo
//                    var chapterLogoUrl:String =result.getJSONObject("chapter")!!.getJSONObject("logo")!!.getString("url")!!.ifEmpty { "" }.toString()
//                    val chapterLogo=Logo(chapterLogoUrl)

                    val chapterRelativeUrl=result.getJSONObject("chapter").getString("relative_url").toString()
                    val chapterState=result.getJSONObject("chapter").getString("state")
                    val timezone=result.getJSONObject("chapter").getString("timezone")
                    val chapterTitle=result.getJSONObject("chapter").getString("title")
                    val chapterUrl=result.getJSONObject("chapter").getString("url")


                    val chapter=Chapter(chapterLocation,chapterCity,chapterCountry,chapterCountryName,chapterDescription,chapterId,chapterRelativeUrl,chapterState,timezone,chapterTitle,chapterUrl,)
                    val title=result.getString("title")
                    val city=result.getString("city")
                    val description=result.getString("description_short").replace(Regex("</.*?\n<p></p>>"),"")
                    val eventTypeTitle=result.getString("event_type_title")
                    val id=result.getString("id").toInt()

//                    val pictureUrl=result.getJSONObject("picture").getString("url")
//                    val picture=Picture(pictureUrl)

                    val startData=result.getString("start_date")
                    val tags= mutableListOf<String>()
                    for(j in 0 until result.getJSONArray("tags").length()){
                        tags.add(result.getJSONArray("tags").getString(j))
                    }
                    val url=result.getString("url")
                    Log.d("title",chapter.toString())
                    val upcomingevent= Result(chapter,city,description,eventTypeTitle,id,startData,
                        tags,title,url)

                    resultArray.add(upcomingevent)
                }
            }) {
                Log.d("UpcomingEventsVolleyError",it.toString())
            }
            queue.add(stringRequest)
        return resultArray

    }



// Add the request to the RequestQueue.

}