package com.example.googledeveloperscommunityvisualisationtool.Repository

import android.util.Log
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Scraping.GdgGroupClasses.GdgGroupDataClass
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class GdgScrapingRespository {
    fun scrape(){
        val url = "https://gdg.community.dev/"

        try {
            // Connect to the web page and retrieve the HTML content
            val doc: Document = Jsoup.connect(url).get()

            val scriptTag: Element? = doc.body().select("script").get(doc.select("script").size-3)
            Log.d("script",scriptTag.toString())

            if (scriptTag != null) {
                // Extract the script content


                // Extract the variable value using regular expressions or string manipulation
//                val variablePattern: Regex = Regex("var localChapters = '(.+?)';")
//                val matchResult: MatchResult? = variablePattern.find(scriptContent)
//                val variableValue: String? = matchResult?.groupValues?.get(1)
//
//                if (scriptContent != null) {
//                    println("Variable Value: $scriptContent")
//                } else {
//                    println("Variable not found")
//                }
            } else {
                println("Script tag not found")
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}