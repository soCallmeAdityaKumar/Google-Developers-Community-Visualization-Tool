package com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents

import android.animation.TimeAnimator.TimeListener
import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.ViewModel
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.GDGDetails
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.PastEvents
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.UpcomingEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.net.URL
import java.sql.Time

class HomeViewModel : ViewModel() {

    private  lateinit var gdgDetails:GDGDetails
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
                    gdgURL.dropLast(1) + pastEventsList[event].getElementsByClass("vertical-box-container __bds-thumbnail-roundness")
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


            val upcomingeventsList = doc.getElementsByClass("row event")
            for (upcomEvent in 0 until upcomingeventsList.size) {
                val link =
                    gdgURL.dropLast(1) + upcomingeventsList[upcomEvent].getElementsByClass("btn btn-primary purchase-ticket")
                        .attr("href")
                val date =
                    upcomingeventsList[upcomEvent].getElementsByClass("date general-body--color")
                        .text()
                val title =
                    upcomingeventsList[upcomEvent].getElementsByClass("general-body--color").text()
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

        return


    }
    suspend fun makeRequests(url: String, totalRequests: Int, delayMillis: Long, retryAttempts: Int){
        var counter = 0
        var currentDelay = delayMillis

        while (counter < totalRequests) {
            try {
                getGdgDetails(url)
                counter++
                println("Request ${gdgDetails.gdgName} completed.")
                break
            } catch (e: Exception) {
                if (e is java.io.IOException && counter < totalRequests) {
                    println("Error occurred: ${e.message}")
                    println("Retrying after delay...")
                    delay(currentDelay)
                    currentDelay *= 2
                    continue
                } else {
                    println("Maximum retry attempts reached. Exiting.")
                    break
                }
            }

            delay(delayMillis)
        }

        println("All requests completed.")
    }

    suspend fun getResponse(url:String):GDGDetails  {
        val totalRequests = 1
        val delayMillis = 0L
        val retryAttempts = 1

        makeRequests(url, totalRequests, delayMillis, retryAttempts)
        return gdgDetails
    }

}
