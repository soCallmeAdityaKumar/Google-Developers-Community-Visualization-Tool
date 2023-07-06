package com.example.googledeveloperscommunityvisualisationtool.dataFetching.gdgChapters

import android.text.TextUtils.replace
import android.util.Log
import com.example.googledeveloperscommunityvisualisationtool.dataClass.gdgGroupClasses.Banner
import com.example.googledeveloperscommunityvisualisationtool.dataClass.gdgGroupClasses.GdgDataClass
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.GDGDetails
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.PastEvents
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.UpcomingEvents
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.jsoup.Jsoup


class GdgScrapingRespo {
    val baseUrl="https://gdg.community.dev/chapters/"
    private var gdgChapters=ArrayList<GdgDataClass>()

    private  lateinit var gdgDetails: GDGDetails

    fun getGdgChapters(){
        try {
            Log.d("home","inside the get chapter repo")
            val doc = Jsoup.connect(baseUrl).url(baseUrl).get()
//            Log.d("home",doc.body().toString())
            Log.d("value",doc.body().getElementsByTag("script")[1].toString())
            var html=doc.body().getElementsByTag("script")[1].html().replace("var localChapters = ","")
            html="$html"
            val obj= JSONArray(html)
            for(i in 0 until obj.length()){
                val avatar=obj.getJSONObject(i).getString("avatar")
                var thumbnailUrl= obj.getJSONObject(i).getString("banner").drop(10).dropLast(2).replace(Regex("</.*:?\n<p></p>>"), "").replace(Regex("\\\\"),"")
                val city=obj.getJSONObject(i).getString("city")
                val cityName=obj.getJSONObject(i).getString("city_name")
                val country=obj.getJSONObject(i).getString("country")
                val longitude:Double=  obj.getJSONObject(i).optDouble("latitude",0.0)
                val latitude =  obj.getJSONObject(i).optDouble("latitude",0.0)
                val url=obj.getJSONObject(i).getString("url")
//            Log.d("latitude",longitude.toString())
//            Log.d("longitude",latitude.toString())
                if(thumbnailUrl.isNotEmpty()){
                    thumbnailUrl
                    thumbnailUrl="https://res.cloudinary.com/startup-grind/image/upload/c_fit,dpr_2,f_auto,g_center,h_400,q_auto:good,w_400/v1/gcs"+thumbnailUrl
                }

                val gdgDataClass=
                    GdgDataClass(avatar,
                        Banner(thumbnailUrl),city,cityName,country, latitude,longitude,url)
//            Log.d("home",gdgGroupDataClassItem.toString())
                gdgChapters.add(gdgDataClass)
            }

        }
        catch (e:Exception){
            Log.d("GDGScrapingUrl",e.message.toString())
        }

//        Log.d("gdgChapters",gdgChapters.size.toString())
    }

    fun returnchapter():ArrayList<GdgDataClass>{
        return gdgChapters
    }
    fun getGdgDetails( gdgURL: String) {

        val pastEventList = ArrayList<PastEvents>()
        val upcomingEventsList = ArrayList<UpcomingEvents>()
        val organizers = ArrayList<Organizers>()


        val doc = Jsoup.connect(gdgURL).get()
        val gdgName = doc.select("h1").get(0).text()
        val groupMember = doc.getElementsByClass("group-members").text()


        val about = doc.getElementsByClass("general-body").text()

        //Past Events
        val pastEventsList = doc.getElementById("past-events-container")?.getElementsByTag("a")
        for (event in 0 until pastEventsList!!.size) {
            val link =
                "https://gdg.community.dev" + pastEventsList[event].getElementsByClass("vertical-box-container __bds-thumbnail-roundness")
                    .attr("href")
            val date =
                pastEventsList[event].getElementsByClass("vertical-box--event-date").text()
            val type =
                pastEventsList[event].getElementsByClass("event-page vertical-box--event-type")
                    .text()
            val title =
                pastEventsList[event].getElementsByClass("event-page vertical-box--event-title")
                    .text()

            pastEventList.add(PastEvents(title, date, type, link))
        }


        val upcomingeventsList = doc.getElementsByClass("row event ")

        for (upcomEvent in 0 until upcomingeventsList.size) {
            val link =
                "https://gdg.community.dev" + upcomingeventsList[upcomEvent].getElementsByClass("btn btn-primary purchase-ticket")
                    .attr("href")
            val date =
                upcomingeventsList[upcomEvent].getElementsByClass("date general-body--color")
                    .text()
            val title =
                upcomingeventsList[upcomEvent].getElementsByTag("h4").text()
            val description =
                upcomingeventsList[upcomEvent].getElementsByClass("description general-body--color")
                    .text()
            upcomingEventsList.add(UpcomingEvents(title, date, link, description))
        }

        //Organizers
        val organizersList = doc.getElementsByClass("people-card general-card")
        for (organizer in 0 until organizersList.size) {
            val name = organizersList[organizer].getElementsByClass("people-card--name").text()
            val company =
                organizersList[organizer].getElementsByClass("people-card--company").text()
            val title =
                organizersList[organizer].getElementsByClass("people-card--title").text()
            val image = organizersList[organizer].getElementsByTag("img").attr("src")

            organizers.add(Organizers(name, company, title, image))

        }
        gdgDetails = GDGDetails(
            gdgName,
            groupMember,
            about,
            pastEventList,
            upcomingEventsList,
            organizers
        )



    }
    suspend fun makeRequests(url: String, totalRequests: Int, delayMillis: Long, retryAttempts: Int){
        var counter = 0
        var currentDelay = delayMillis

        while (counter < retryAttempts) {
            try {
                getGdgDetails(url)
                counter++
                println("Request ${gdgDetails.gdgName} completed.")
                break
            } catch (e: Exception) {
                if (e is java.io.IOException && counter < retryAttempts) {
                    println("Error occurred: ${e.message}")
                    println("Retrying after delay...")
                    delay(currentDelay)
                    currentDelay *= 2
                } else {
                    println("Maximum retry attempts reached. Exiting.")
                    break
                }
            }
            delay(delayMillis)
        }

        println("All requests completed.")
    }

    suspend fun getResponse(url:String): GDGDetails {
        val totalRequests = 969
        val delayMillis = 0L
        val retryAttempts = 2

        makeRequests(url, totalRequests, delayMillis, retryAttempts)
        return gdgDetails
    }

}