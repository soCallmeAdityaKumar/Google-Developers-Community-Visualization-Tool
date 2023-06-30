package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents.detailsUpcomingEvents.dataItem.upcomEventData
import org.jsoup.Jsoup
import java.io.IOException

class upcoEventsDetailsRepo{
private  var details=upcomEventData("","","","","","","", setOf())
    fun getResponse(baseurl:String,context: Context){
        try {
            val doc = Jsoup.connect(baseurl).get()
            //event title
            val title = doc.getElementsByClass("font_banner2").text()
            val address = doc.getElementsByClass("city-date").get(0)
                .getElementsByClass("event-header-address").text()
                .replace("- View Map".toRegex(), "") ?: ""
            val gdgname = doc.getElementsByClass("event-chapter-title").text()
            val dateAndTime = doc.getElementsByClass("event-date-time").text()
            val rsvp = doc.getElementsByClass("event-attendees").text() ?: ""
            val desc = doc.getElementsByClass("event-description general-body").text()
            val duration = doc.getElementsByClass("title-span").text() ?: ""
            //mentors
            val mentors = mutableListOf<Organizers>()
            val people = doc.getElementsByClass("people-card general-card")
            for (i in 0 until people.size) {
                val name = people[i].getElementsByClass("people-card--name").text()
                val company = people[i].getElementsByClass("people-card--company").text()
                val title = people[i].getElementsByClass("people-card--title").text()
                val image = people[i].getElementsByTag("img").attr("src")
                mentors.add(Organizers(name, company, title, image))
                Log.d("mentor", "$name,$company,$title,$image")
            }
            details = upcomEventData(
                title,
                address,
                gdgname,
                dateAndTime,
                rsvp,
                desc,
                duration,
                mentors.toSet()
            )
        }catch (e:IOException){
            e.printStackTrace()
            Log.d("Page not found", e.message.toString()+","+ (e.localizedMessage?.toString()))
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context,"Please refresh your upcoming events list",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun returndetails():upcomEventData{
        return details
    }
}