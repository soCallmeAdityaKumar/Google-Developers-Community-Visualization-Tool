package com.example.googledeveloperscommunityvisualisationtool.DataFetching.GdgChapters

import android.util.Log
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.Banner
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.GdgGroupDataClassItem
import org.json.JSONArray
import org.jsoup.Jsoup

const val baseUrl="https://gdg.community.dev/chapters/"
class GdgScrapingRespository {
    private var gdgChapters=ArrayList<GdgGroupDataClassItem>()
    fun getGdgChapters():ArrayList<GdgGroupDataClassItem>{
        val doc = Jsoup.connect(baseUrl).get()
        Log.d("value",doc.body().getElementsByTag("script")[1].toString())
        var html=doc.body().getElementsByTag("script")[1].html().replace("var localChapters = ","")
        html="$html"
        val obj= JSONArray(html)
        for(i in 0 until obj.length()){
            val avatar=obj.getJSONObject(i).getString("avatar")
            val banner=Banner(obj.getJSONObject(i).getString("banner"))
            val city=obj.getJSONObject(i).getString("city")
            val cityName=obj.getJSONObject(i).getString("city_name")
            val country=obj.getJSONObject(i).getString("country")
            val longitude:Double=  obj.getJSONObject(i).optDouble("latitude",0.0)
            val latitude =  obj.getJSONObject(i).optDouble("latitude",0.0)
            val url=obj.getJSONObject(i).getString("url")
//            Log.d("latitude",longitude.toString())
//            Log.d("longitude",latitude.toString())

            val gdgGroupDataClassItem=GdgGroupDataClassItem(avatar,banner,city,cityName,country, latitude,longitude,url)
            gdgChapters.add(gdgGroupDataClassItem)
        }
//        Log.d("gdgChapters",gdgChapters.size.toString())
        return gdgChapters
    }
}