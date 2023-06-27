package com.example.googledeveloperscommunityvisualisationtool.dataClass.volley

data class Result(
//    val allows_cohosting: Boolean,
      val chapter: Chapter,
      val city: String,
      val description_short: String,
//    val event_type_logo: EventTypeLogo,
      val event_type_title: String,
      val id: Int,
//    val picture: Picture?,
//    val relative_url: String,
//    val result_type: String,
      val start_date: String,
      val tags: List<String>,
      val title: String,
      val url: String,
//    val video_url: Any
)