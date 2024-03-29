package com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.Movement.Movement
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.Shape.Point
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.Shape.Shape
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.balloon.Balloon
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POI
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.poi.POICamera
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.events
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.Organizers
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.PastEvents
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.home.UpcomingEvents
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ActionBuildCommandUtility {
    private const val TAG_DEBUG = "ActionBuildCommandUtility"
    private const val BASE_PATH = "/var/www/html/"
    var RESOURCES_FOLDER_PATH = BASE_PATH + "resources/"

    /**
     * Build the command to fly to the position
     * @param poi POI with the information
     * @return String with the command
     */
    @JvmStatic
    fun buildCommandPOITest(poi: POI): String {
        val poiLocation = poi.poiLocation
        val poiCamera = poi.poiCamera
        return "echo 'flytoview=" +
                "<gx:duration>" + poiCamera!!.duration + "</gx:duration>" +
                "<gx:flyToMode>smooth</gx:flyToMode>" +
                "<LookAt>" +
                "<longitude>" + poiLocation!!.longitude + "</longitude>" +
                "<latitude>" + poiLocation.latitude + "</latitude>" +
                "<altitude>" + poiLocation.altitude + "</altitude>" +
                "<heading>" + poiCamera.heading + "</heading>" +
                "<tilt>" + poiCamera.tilt + "</tilt>" +
                "<range>" + poiCamera.range + "</range>" +
                "<gx:altitudeMode>" + poiCamera.altitudeMode + "</gx:altitudeMode>" +
                "</LookAt>' > /tmp/query.txt"
    }

    /**
     * @return Command to create the resources fule
     */
    @JvmStatic
    fun buildCommandCreateResourcesFolder(): String {
        return "mkdir -p " + RESOURCES_FOLDER_PATH
    }

    /**
     * @return Command to write the path to the balloon.kml
     */
    @JvmStatic
    fun buildWriteBalloonFile(): String {
        val command = "echo 'http://lg1:81/slave_2.kml'  > " +
                BASE_PATH +
                "kmls.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }
    @JvmStatic
    fun buildWritePlacemarkFile(): String {
        val command = "echo 'http://lg1:81/slave_1.kml'  > " +
                BASE_PATH +
                "kmls.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    /**
     * Build the command to paint a balloon in Liquid Galaxy
     * @return String with command
     */
    @JvmStatic
    fun buildCommandBalloonWithLogos(): String {
        val startCommand = "echo '"+
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                "xmlns:atom=\"http://www.w3.org/2005/Atom\" \n" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\"> \n" +
                "<Document id=\"slave_3\">\n" +
                " <Folder> \n" +
                " <name>Logos</name> \n" +
                "  <ScreenOverlay>\n" +
                "  <name>Logo</name> \n" +
                "  <Icon> \n" +
                "   <href>https://raw.githubusercontent.com/soCallmeAdityaKumar/Google-Developers-Community-Visualization-Tool/main/app/src/main/assets/splashimage.png</href> \n" +
                "  </Icon> \n" +
                "    <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
                "  <overlayXY x=\"0\" y=\"1\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                "  <screenXY x=\"0.02\" y=\"0.95\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                "  <rotationXY x=\"0\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/> \n" +
                "  <size x=\"500\" y=\"500\" xunits=\"pixels\" yunits=\"pixels\"/> \n" +
                "  </ScreenOverlay> \n" +
                " </Folder> \n" +
                "</Document> \n" +
                "</kml>\n" +
                "' > " +
                BASE_PATH +
                "kml/slave_3.kml"
        Log.w(TAG_DEBUG, "Command: $startCommand")
        return startCommand
    }

    /**
     * Build the command to paint a balloon in Liquid Galaxy
     * @param balloon Balloon with the information to be send
     * @return String with command
  */
//    @JvmStatic
//    fun buildCommandBalloonTest(balloon: Balloon): String {
//        val poi = balloon.poi
//        val TEST_PLACE_MARK_ID = "testPlaceMark12345"
//        val startCommand = "echo '"+
//                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
//                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
//                "\n" +
//                " <Document>\n" +
//                " <Placemark id=\"" + TEST_PLACE_MARK_ID + "\">\n" +
//                "    <name>" + balloon.poi?.poiLocation?.name + "</name>\n" +
//                "    <description>\n" +
//                "<![CDATA[\n" +
//                "  <head>\n" +
//                "    <!-- Required meta tags -->\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
//                "\n" +
//                "    <!-- Bootstrap CSS -->\n" +
//                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
//                "\n" +
//                "  </head>\n" +
//                "  <body>\n" +
//                "    <div class=\"p-lg-5\" align=\"center\">\n" +
//                "\n"
//        var description = ""
//        if (balloon.description != "") {
//            description =  "        <h5>" + balloon.description + "</h5>\n" +
//                    "        <br>\n"
//        }
//        var imageCommand = ""
//        if (balloon.imagePath != null && balloon.imagePath != "") {
//            imageCommand =  "        <img src=\"./resources/" + getFileName(balloon.imagePath) + "\"> \n" +
//                    "        <br>\n"
//        }
//        var videoCommand = ""
//        if (balloon.videoPath != null && balloon.videoPath != "") {
//            videoCommand = "<iframe" +
//                    " src=\""+ balloon.videoPath + "\" frameborder=\"0\"" +
//                    " allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen>" +
//                    "</iframe>"
//        }
//        val endCommand = "    </div>\n    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
//                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
//                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" +
//                "  </body>\n" +
//                "]]>" +
//                "    </description>\n" +
//                "    <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
//                "    <Point>\n" +
//                "      <coordinates>" + poi?.poiLocation?.longitude + "," + poi?.poiLocation?.latitude + "</coordinates>\n" +
//                "    </Point>\n" +
//                "  </Placemark>\n" +
//                "</Document>\n" +
//                "</kml>" +
//                "' > " +
//                BASE_PATH +
//                "kml/slave_2.kml"
//        Log.w(TAG_DEBUG, startCommand + description + imageCommand + videoCommand + endCommand)
//        return startCommand + description + imageCommand + videoCommand + endCommand
//    }
    @JvmStatic
    fun buildCommandBalloonTest(balloon: Balloon): String {
        val poi = balloon.poi
        val organizerlistType=object : TypeToken<List<Organizers>>() {}.type
        val pasteventlistType=object : TypeToken<List<PastEvents>>() {}.type
        val upcoeventlistType=object : TypeToken<List<UpcomingEvents>>() {}.type
        var organizersList= mutableListOf<Organizers>()
        var upcomingEventList= listOf<UpcomingEvents>()
        var pastEventList= listOf<PastEvents>()
        if(balloon.stringOrganizser!!.isNotEmpty())
         organizersList=Gson().fromJson(balloon.stringOrganizser,organizerlistType)
        if(balloon.stringUpcomingEvent!!.isNotEmpty())
         upcomingEventList=Gson().fromJson(balloon.stringUpcomingEvent,upcoeventlistType)
        if(balloon.stringPastEvent!!.isNotEmpty())
         pastEventList=Gson().fromJson(balloon.stringPastEvent,pasteventlistType)
        Log.d("build",balloon.stringOrganizser.toString()+","+organizersList.size)
        Log.d("build",balloon.stringPastEvent.toString()+","+pastEventList.size)
        Log.d("build",balloon.stringUpcomingEvent.toString()+","+upcomingEventList.size)

        val TEST_PLACE_MARK_ID = "testPlaceMark12345"
        var startCommand = "echo '"+
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
                " <Document>\n" +
                "    <name>helloBalloon.kml</name>\n" +
                "<Style id=\"purple_paddle\">\n"+
                "<IconStyle>\n"+
                "<Icon>\n"+
                "<href>https://raw.githubusercontent.com/soCallmeAdityaKumar/Google-Developers-Community-Visualization-Tool/main/app/src/main/res/drawable/googledeveloper_placemark.png</href>\n"+
                "</Icon>\n"+
                "</IconStyle>\n"+
                "<BalloonStyle>\n"+
                "<text>"+balloon.description+"</text>\n"+
                "<bgColor>ff1e1e1e</bgColor>\n"+
                "</BalloonStyle>\n"+
                "</Style>\n"+
                " <Placemark id=\"" + TEST_PLACE_MARK_ID + "\">\n" +
                "<name>"+balloon.name+"</name>\n"+
                "<Snippet maxLines=\"0\"></Snippet>\n"+
                "<description>\n" +
                "<![CDATA[\n" +
                "<table border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\n"+
                "<tr>\n"+
                "<td colspan=\"2\" align=\"center\">\n"+
                "<img src="+ balloon.imageUri+" alt=\"picture\" width=\"400\" height=\"150\" />\n"+
                "</td>\n"+
                "</tr>\n"+
                "<tr>\n"+
                "<td colspan=\"2\" align=\"center\">\n"+
                "<h2><font color=\'#00CC99\'>"+balloon.name+"</font></h2>\n"+
                "<h3><font color=\'#00CC99\'>"+balloon.city+","+balloon.country+"</font></h3>\n"+
                "</td>\n"+
                "</tr>\n"+
//                "<tr>\n"+
//                "<td colspan=\"2\" align=\"center\">\n"+
//                "<img src=\"https://raw.githubusercontent.com/yashrajbharti/kml-images/main/historic_infographic.png\" alt=\"picture\" width=\"400\" height=\"240\" />    </td>\n"+
//                "</tr>\n"+
                "<tr>\n"+
                "<td colspan=\"1\">\n"+
                "<p><h5>About GDG</h5></p>\n"+
                "</td>\n"+
                "</tr>\n"+
                "<tr>\n"+
                "<td colspan=\"2\">\n"+
                "<p><font color=\"#3399CC\">"+balloon.description+"</font></p>\n"+
                "</td>\n"+
                "</tr>\n"+
                "<tr>\n"+
                "<td colspan=\"2\">\n"+
                "<p><h4>Organizers</h4></p>\n"+
                "</td>\n"+
                "</tr>\n"
                var organizersCommand=""
                organizersCommand="<tr><td>No Organizers</td></tr>\n"
                if(organizersList.isNotEmpty()){
                    var organizersNameCommand="<tr>\n"
                    for(organisers in organizersList){
                        organizersNameCommand+="<td>"+organisers.organizername+"</td>"
                    }
                    organizersNameCommand+="</tr>\n<tr>\n"
                    var orgCompanyCommand=""
                    for(company in organizersList){
                        orgCompanyCommand+="<td>"+company.organizercompany+"</td>"
                    }
                    orgCompanyCommand+="</tr>\n<tr>\n"
                    var orgImgCommand=""
                    for(image in organizersList){
                        orgImgCommand+="<td><img src="+image.organizerimage+" alt=\"picture\" width=\"50\" height=\"50\"  /></td>"
                    }
                    orgImgCommand+="</tr>\n"
                    organizersCommand=organizersNameCommand+orgCompanyCommand+orgImgCommand
                }
                var pastDec="<tr>\n"+
                "<td colspan=\"2\">\n"+
                "<p><h4>Past Events</h4></p>\n"+
                "</td>\n"+
                "</tr>\n"
                var pastEventCommand=""
                if(pastEventList.isNotEmpty()){
                    var pasteventname="<tr>\n"
                    for(event in pastEventList){
                        pasteventname+="<td width=\"20\">"+event.pastEventstitle+"</td>"
                    }
                    pasteventname+="</tr>\n<tr>\n"
                    var pastEventType=""
                    for(type in pastEventList){
                        pastEventType+="<td>"+type.pastEventstype+"</td>"
                    }
                    pastEventType+="</tr>\n<tr>\n"
                    var pastDateEvent=""
                    for(date in pastEventList){
                        pastDateEvent+="<td>"+date.pastEventsdate+"</td>"
                    }
                    pastDateEvent+="</tr>\n"
                    pastEventCommand=pasteventname+pastEventType+pastDateEvent
                }else{
                    pastEventCommand="<tr><td>No Past Events</td></tr>\n"
                }
                val UpcoDec="<tr>\n"+
                "<td colspan=\"2\">\n"+
                "<p><h4>Upcoming Events</h4></p>\n"+
                "</td>\n"+
                "</tr>\n"
                var upcoEventcommand=""
                if(upcomingEventList.isNotEmpty()){
                    var upcoEventName="<tr>\n"
                    for(event in upcomingEventList){
                        upcoEventName+="<td>"+event.upcomingEventstitle+"</td>"
                    }
                    upcoEventName+="</tr>\n<tr>\n"
                    var upcoDateEvent=""
                    for(date in upcomingEventList){
                        upcoDateEvent+="<td>"+date.upcomingEventsdate+"</td>"
                    }
                    upcoDateEvent+="</tr>\n"
                    upcoEventcommand=upcoEventName+upcoDateEvent
                }
                else{
                    upcoEventcommand="<tr><td>No Upcoming Event</td></tr>\n"
                }
                val endCommand="<tr>\n"+
                "<td colspan=\"2\" align=\"center\">\n"+
                "<font color=\"#999999\">@Google Developer Visualization Tool 2023</font>\n"+
                "</td>\n"+
                "</tr>\n"+
                "</table>"+
                "]]>" +
                "    </description>\n" +
//                "    <styleUrl>#purple_paddle</styleUrl>\n"+
                "    <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
                "    <Point>\n" +
                "      <coordinates>" + poi?.poiLocation?.longitude + "," + poi?.poiLocation?.latitude + ",0</coordinates>\n" +
                "    </Point>\n" +
                "  </Placemark>\n" +
                "</Document>\n" +
                "</kml>" +
                "' > " +
                BASE_PATH +
                "kml/slave_2.kml"
        Log.w(TAG_DEBUG, startCommand+organizersCommand+pastDec+pastEventCommand+UpcoDec+upcoEventcommand+endCommand)
        return startCommand+organizersCommand+pastDec+pastEventCommand+UpcoDec+upcoEventcommand+endCommand
    }

     fun buildPlacemarkInMaster(poi: POI):String{
         val TEST_PLACE_MARK_ID = "testPlaceMark12345"

         var startCommand = "echo '"+
                 "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                 " xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
                 " <Document>\n" +
                 " <Placemark id=\"" + TEST_PLACE_MARK_ID + "\">\n" +
                 "    <name>helloBalloon.kml</name>\n" +
                 "<Style id=\"purple_paddle\">\n"+
                 "<IconStyle>\n"+
                 "<Icon>\n"+
                 "<href>https://raw.githubusercontent.com/soCallmeAdityaKumar/Google-Developers-Community-Visualization-Tool/main/app/src/main/res/drawable/googledeveloper_placemark.png</href>\n"+
                 "</Icon>\n"+
                 "</IconStyle>\n"+
                 "</Style>\n"+
                 "<name>Google Developer Group</name>\n"+
//                "    <styleUrl>#purple_paddle</styleUrl>\n"+
                 "    <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
                 "    <Point>\n" +
                 "      <coordinates>" + poi?.poiLocation?.longitude + "," + poi?.poiLocation?.latitude + ",0</coordinates>\n" +
                 "    </Point>\n" +
                 "  </Placemark>\n" +
                 "</Document>\n" +
                 "</kml>" +
                 "' > " +
                 BASE_PATH +
                 "kml/slave_1.kml"
        Log.w(TAG_DEBUG,startCommand)
        return startCommand

    }

//    @JvmStatic
//    fun buildCommandBalloonTest(balloon: Balloon): String {
//        val poi = balloon.poi
//        val TEST_PLACE_MARK_ID = "testPlaceMark12345"
//        val startCommand = "echo '"+
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
//        "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n"+
//        "<Document>\n"+
//        "<name>historic.kml</name>\n"+
//        "<Style id=\"purple_paddle\">\n"+
//        "<IconStyle>\n"+
//        "<Icon>\n"+
//        "<href>https://raw.githubusercontent.com/yashrajbharti/kml-images/main/molten.png</href>\n"+
//        "</Icon>\n"+
//        "</IconStyle>\n"+
//        "<BalloonStyle>\n"+
//        "<text>$[description]</text>\n"+
//        "<bgColor>ff1e1e1e</bgColor>\n"+
//        "</BalloonStyle>\n"+
//        "</Style>\n"+
//        "<Placemark id=\""+TEST_PLACE_MARK_ID+"\">\n"+
//        "<name>Historic Track Map</name>\n"+
//        "<Snippet maxLines=\"0\"></Snippet>\n"+
//        "<description><![CDATA[" +
//        "<!-- BalloonStyle background color:\"ffffffff\"-->\n"+
//        "<!-- Icon URL:\"http://maps.google.com/mapfiles/kml/paddle/purple-blank.png\"-->"+
//        "<table width=\"400\" border=\"0\" cellspacing=\"0\" cellpadding=\"5\">\n"+
//        "<tr>\n"+
//        "<td colspan=\"2\" align=\"center\">\n"+
//        "<img src=\"https://raw.githubusercontent.com/yashrajbharti/kml-images/main/volcano.png\" alt=\"picture\" width=\"150\" height=\"150\" />\n"+
//        "</td>\n"+
//        "</tr>\n"+
//        "<tr>\n"+
//        "<td colspan=\"2\" align=\"center\">\n"+
//        "<h2><font color=\'#00CC99\'>Historic Track Map</font></h2>\n"+
//        "<h3><font color=\'#00CC99\'>Various years between 1480 and 2021</font></h3>\n"+
//        "</td>\n"+
//        "</tr>\n"+
//        "<tr>\n"+
//        "<td colspan=\"2\" align=\"center\">\n"+
//        "<img src=\"https://raw.githubusercontent.com/yashrajbharti/kml-images/main/historic_infographic.png\" alt=\"picture\" width=\"400\" height=\"240\" />    </td>\n"+
//        "</tr>\n"+
//        "<tr>\n"+
//        "<td colspan=\"2\">\n"
//        "<p><font color=\"#3399CC\">"+balloon.description+"</font></p>\n"+
//        "</td>\n"+
//        "</tr>\n"+
//        "<tr>\n"+
//        "<td align=\"center\">\n"+
//        "<a href=\"#\">COPERNICUS, ResearchGate, Global Volcanism Program </a>\n"+
//        "</td>\n"+
//        "</tr>\n"+
//        "<tr>\n"+
//        "<td colspan=\"2\" align=\"center\">\n"+
//        "<font color=\"#999999\">@La Palma Volcano Tracker 2022</font>\n"+
//        "</td>\n"+
//        "</tr>\n"+
//        "</table>]]></description>\n"+
//        "<LookAt>\n"+
//        "<longitude>\""+poi?.poiLocation?.longitude+"\"</longitude>\n"+
//        "<latitude>\""+poi?.poiLocation?.longitude+"\"</latitude>\n"+
//        "<altitude>0</altitude>\n"+
//        "<heading>0</heading>\n"+
//        "<tilt>0</tilt>\n"+
//        "<range>24000</range>\n"+
//        "</LookAt>\n"+
//        "<styleUrl>#purple_paddle</styleUrl>\n"+
//        "<gx:balloonVisibility>1</gx:balloonVisibility>\n"+
//        "<Point>\n"+
//        "<coordinates>"+poi?.poiLocation?.latitude+","+poi?.poiLocation?.longitude+"0"+"</coordinates>\n"+
//        "</Point>\n"+
//        "</Placemark>\n"+
//        "</Document>\n"+
//        "</kml>\n" +
//                "' > " +
//                BASE_PATH +
//                "kml/slave_2.kml"
//        Log.w(TAG_DEBUG, startCommand )
//        return startCommand
//    }

    /**
     * Get the absolute path of the file
     * @param filePath The path of the file
     * @return the absolute path
     */
    private fun getFileName(filePath: String?): String {
        val route = filePath!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return route[route.size - 1]
    }


    /**
     * @return Command to write the path to the shape.kml
     */
    @JvmStatic
    fun buildWriteShapeFile(): String {
        val command = "echo 'http://lg1:81/shape.kml' > " +
                BASE_PATH +
                "kmls.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    /**
     * Build the command to paint the shape in liquid galaxy
     * @param shape Shape with the information
     * @return Command to paint the shape in Liquid Galaxy
     */
    @JvmStatic
    fun buildCommandSendShape(shape: Shape): String {
        val command = StringBuilder()
        command.append("echo '").append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            .append("<kml xmlns=\"http://www.opengis.net/kml/2.2\"> \n")
            .append(
                "<Document>\n" +
                        "  <name>shape.kml</name>\n" +
                        "  <open>1</open>\n")
            .append(
                        "  <Style id=\"linestyleExample\">\n" +
                        "    <LineStyle>\n" +
                        "      <color>501400FF</color>\n" +
                        "      <width>100</width>\n" +
                        "      <gx:labelVisibility>1</gx:labelVisibility>\n" +
                        "    </LineStyle>\n" +
                        " </Style>\n")
            .append("<Placemark>\n").append("<styleUrl>#linestyleExample</styleUrl>\n")
            .append("<name>").append(shape.poi!!.poiLocation!!.name).append("</name>\n")
            .append("<LineString>\n")
        if (shape.isExtrude) command.append("<extrude>1</extrude>\n")
        command.append("<tessellate>1</tessellate>\n")
            .append("<altitudeMode>absolute</altitudeMode>\n")
            .append("<coordinates>\n")
        val points = shape.points
        val pointsLength = points!!.size
        var point: Point
        for (i in 0 until pointsLength) {
            point = points[i] as Point
            command.append("    ").append(point.longitude).append(",").append(point.latitude)
                .append(",").append(point.altitude).append("\n")
        }
        command.append("</coordinates>\n").append("</LineString>\n").append("</Placemark>\n")
            .append("</Document>\n").append("</kml> " + "' > ")
            .append(BASE_PATH).append("shape.kml")
        Log.w(TAG_DEBUG, "Command: $command")
        return command.toString()
    }

    /**
     * @return Command to clean the kmls.txt
     */
    @JvmStatic
    fun buildCleanKMLs(): String {
        val command = "echo '' > " +
                BASE_PATH +
                "kmls.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    /**
     * @return Command to clean the query.txt
     */
    @JvmStatic
    fun buildCleanQuery(): String {
        val command = "echo '' > /tmp/query.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    @JvmStatic
    fun buildCommandOrbit(poi: POI?): String {
        val command = StringBuilder()
        command.append("echo '").append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            .append("<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n")
            .append("xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\"> \n")
            .append("<gx:Tour> \n").append(" <name>Orbit</name> \n").append(" <gx:Playlist> \n")
        orbit(poi, command)
        command.append(" </gx:Playlist>\n")
            .append("</gx:Tour>\n").append("</kml> " + "' > ")
            .append(BASE_PATH).append("Orbit.kml")
        Log.w(TAG_DEBUG, "Command: $command")
        return command.toString()
    }

    @JvmStatic
    fun buildCommandWriteOrbit(): String {
        val command = "echo 'http://lg1:81/Orbit.kml'  > " +
                BASE_PATH +
                "kmls.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    @JvmStatic
    fun buildCommandStartOrbit(): String {
        val command = "echo \"playtour=Orbit\" > /tmp/query.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    @JvmStatic
    fun buildCommandTour(actions: List<Action>): String {
        val startCommand = "echo '" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
                "<Document>\n" +
                "  <name>Tour</name>\n" +
                "  <open>1</open>\n\n" +
                "  <gx:Tour>\n" +
                "    <name>TestTour</name>\n" +
                "    <gx:Playlist>\n\n"

        //Build the tour
        val folderBalloonShapes = StringBuilder()
        folderBalloonShapes.append("  <Folder>\n" +
                "   <name>Points and Shapes</name>\n\n").append(
                    "   <Style id=\"linestyleExample\">\n" +
                    "    <LineStyle>\n" +
                    "    <color>501400FF</color>\n" +
                    "    <width>100</width>\n" +
                    "    <gx:labelVisibility>1</gx:labelVisibility>\n" +
                    "    </LineStyle>\n" +
                    "   </Style>\n\n")
        val middleCommand = buildTour(actions, folderBalloonShapes)
        folderBalloonShapes.append(" </Folder>\n")
        folderBalloonShapes.append(
            "</Document>\n" + "</kml> ' > ").append(BASE_PATH).append("Tour.kml")
        Log.w(TAG_DEBUG, "FOLDER COMMAND: $folderBalloonShapes")
        val endCommand = """    </gx:Playlist>
  </gx:Tour>

"""
        Log.w(
            TAG_DEBUG,
            "FINAL COMMAND: $startCommand$middleCommand$folderBalloonShapes$endCommand"
        )
        return startCommand + middleCommand + endCommand + folderBalloonShapes.toString()
    }

    private fun buildTour(actions: List<Action>, folderBalloonShapes: StringBuilder): String {
        val command = StringBuilder()
        var action: Action
        for (i in actions.indices) {
            action = actions[i]
            if (action is POI) {
                command.append(POICommand(action))
            } else if (action is Movement) {
                command.append(MovementCommand(action))
            } else if (action is Balloon) {
                command.append(BalloonCommand(action, i, folderBalloonShapes))
            } else if (action is Shape) {
                command.append(ShapeCommand(action, i, folderBalloonShapes))
            }
        }
        return command.toString()
    }

    private fun POICommand(poi: POI): String {
        val poiLocation = poi.poiLocation
        val poiCamera = poi.poiCamera
        val command =  "     <gx:FlyTo>\n" +
                "      <gx:duration>" + poiCamera?.duration + "</gx:duration>\n" +
                "      <gx:flyToMode>bounce</gx:flyToMode>\n" +
                "      <LookAt>\n" +
                "       <longitude>" + poiLocation?.longitude + "</longitude>\n" +
                "       <latitude>" + poiLocation?.latitude + "</latitude>\n" +
                "       <altitude>" + poiLocation?.altitude + "</altitude>\n" +
                "       <heading>" + poiCamera?.heading + "</heading>\n" +
                "       <tilt>" + poiCamera?.tilt + "</tilt>\n" +
                "       <range>" + poiCamera?.range + "</range>\n" +
                "       <gx:altitudeMode>" + poiCamera?.altitudeMode + "</gx:altitudeMode>\n" +
                "     </LookAt>\n" +
                "    </gx:FlyTo>\n\n"
        Log.w(TAG_DEBUG, "POI COMMAND: $command")
        return command
    }

    private fun MovementCommand(movement: Movement): String {
        val poi = movement.poi
        val command = StringBuilder()
        if (movement.isOrbitMode) {
            orbit(poi, command)
            Log.w(TAG_DEBUG, "ORBIT COMMAND: $command")
        } else {
            val camera = poi!!.poiCamera
            val poiCamera = POICamera(
                camera!!.heading,
                camera.tilt,
                camera.range,
                camera.altitudeMode,
                camera.duration
            )
            val poiSend = POI()
            poiCamera.heading = movement.newHeading
            poiCamera.tilt = movement.newTilt
            poiSend.setPoiCamera(poiCamera)
            poiSend.setPoiLocation(poi.poiLocation)
            movement(poiSend, command, movement.duration)
            Log.w(TAG_DEBUG, "MOVEMENT COMMAND: $command")
        }
        return command.toString()
    }

    private fun orbit(poi: POI?, command: StringBuilder) {
        var heading = poi!!.poiCamera!!.heading
        var orbit = 0
        while (orbit <= 36) {
            if (heading >= 360) heading = heading - 360
            command.append("    <gx:FlyTo>\n").append("    <gx:duration>1.2</gx:duration> \n")
                .append("    <gx:flyToMode>smooth</gx:flyToMode> \n")
                .append("     <LookAt> \n")
                .append("      <longitude>").append(poi.poiLocation!!.longitude)
                .append("</longitude> \n")
                .append("      <latitude>").append(poi.poiLocation!!.latitude)
                .append("</latitude> \n")
                .append("      <heading>").append(heading).append("</heading> \n")
                .append("      <tilt>").append(60).append("</tilt> \n")
                .append("      <gx:fovy>35</gx:fovy> \n")
                .append("      <range>").append(poi.poiCamera!!.range).append("</range> \n")
                .append("      <gx:altitudeMode>absolute</gx:altitudeMode> \n")
                .append("      </LookAt> \n")
                .append("    </gx:FlyTo> \n\n")
            heading = heading + 10
            orbit++
        }
    }

    private fun movement(poi: POI, command: StringBuilder, duration: Int) {
        val poiLocation = poi.poiLocation
        val poiCamera = poi.poiCamera
        command.append("    <gx:FlyTo>\n")
            .append("    <gx:duration>").append(duration).append("</gx:duration>\n")
            .append("    <gx:flyToMode>smooth</gx:flyToMode>\n")
            .append("     <LookAt>\n")
            .append("      <longitude>").append(poiLocation!!.longitude).append("</longitude>\n")
            .append("      <latitude>").append(poiLocation.latitude).append("</latitude>\n")
            .append("      <altitude>").append(poiLocation.altitude).append("</altitude>\n")
            .append("      <heading>").append(poiCamera!!.heading).append("</heading>\n")
            .append("      <tilt>").append(poiCamera.tilt).append("</tilt>\n")
            .append("      <range>").append(poiCamera.range).append("</range>\n")
            .append("      <gx:altitudeMode>").append(poiCamera.altitudeMode)
            .append("</gx:altitudeMode>\n")
            .append("     </LookAt>\n")
            .append("    </gx:FlyTo>\n\n")
    }

    private fun BalloonCommand(
        balloon: Balloon,
        position: Int,
        folderBalloonShapes: StringBuilder
    ): String {
        val poi = balloon.poi
        val TEST_PLACE_MARK_ID = "balloon$position"
        val animate = "    <gx:AnimatedUpdate>\n" +
                "    <gx:duration>0</gx:duration>\n" +
                "     <Update>\n" +
                "      <targetHref/>\n" +
                "      <Change>\n" +
                "       <Placemark targetId=\"" +  TEST_PLACE_MARK_ID  + "\">\n" +
                "        <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
                "       </Placemark>\n" +
                "      </Change>\n" +
                "     </Update>\n" +
                "    </gx:AnimatedUpdate>\n\n"
        val startCommand = "    <Placemark id=\"" + TEST_PLACE_MARK_ID + "\">\n" +
                "     <name>" + balloon.poi?.poiLocation?.name + "</name>\n" +
                "     <description>\n" +
                "      <![CDATA[\n" +
                "      <head>\n" +
                "      <!-- Required meta tags -->\n" +
                "      <meta charset=\"UTF-8\">\n" +
                "      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "\n" +
                "      <!-- Bootstrap CSS -->\n" +
                "      <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                "\n" +
                "      </head>\n" +
                "      <body>\n" +
                "       <div class=\"p-lg-5\" align=\"center\">\n" +
                "\n"
        var description = ""
        if (balloon.description != "") {
            description = "       <h5>" + balloon.description + "</h5>\n" +
                    "       <br>\n"
        }
        var imageCommand = ""
        if (balloon.imagePath != null && balloon.imagePath != "") {
            imageCommand = "       <img src=\"./resources/" + getFileName(balloon.imagePath) + "\"> \n" +
                    "       <br>\n"
        }
        var videoCommand = ""
        if (balloon.videoPath != null && balloon.videoPath != "") {
            videoCommand = "       <iframe" +
                    " src=\""+ balloon.videoPath + "\" frameborder=\"0\"" +
                    " allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen>" +
                    "</iframe>"
        }
        val endCommand = "       </div>\n    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "       <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
                "       <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" +
                "       </body>\n" +
                "       ]]>\n" +
                "      </description>\n" +
                "      <Point>\n" +
                "       <coordinates>" + poi?.poiLocation?.longitude + "," + poi?.poiLocation?.latitude + "</coordinates>\n" +
                "      </Point>\n" +
                "    </Placemark>\n\n"
        val wait = commandWait(balloon.duration)
        folderBalloonShapes.append(startCommand + description + imageCommand + videoCommand + endCommand)
        Log.w(TAG_DEBUG, "BALLOON: $folderBalloonShapes")
        val animateClose =  "    <gx:AnimatedUpdate>\n" +
                "    <gx:duration>0</gx:duration>\n" +
                "     <Update>\n" +
                "      <targetHref/>\n" +
                "      <Change>\n" +
                "       <Placemark targetId=\"" +  TEST_PLACE_MARK_ID  + "\">\n" +
                "        <gx:balloonVisibility>0</gx:balloonVisibility>\n" +
                "       </Placemark>\n" +
                "      </Change>\n" +
                "     </Update>\n" +
                "    </gx:AnimatedUpdate>\n\n"
        return animate + wait + animateClose
    }

    private fun ShapeCommand(
        shape: Shape,
        position: Int,
        folderBalloonShapes: StringBuilder
    ): String {
        val TEST_PLACE_MARK_ID = "shape$position"
        val animate = "    <gx:AnimatedUpdate>\n" +
                "    <gx:duration>0</gx:duration>\n" +
                "     <Update>\n" +
                "      <targetHref/>\n" +
                "       <Change>\n" +
                "       <Placemark targetId=\"" +  TEST_PLACE_MARK_ID  + "\">\n" +
                "        </Placemark>\n" +
                "       </Change>\n" +
                "      </Update>\n" +
                "    </gx:AnimatedUpdate>\n\n"
        val command = StringBuilder()
        command.append("     <Placemark id=\"").append(TEST_PLACE_MARK_ID).append("\">\n")
            .append("      <styleUrl>#linestyleExample</styleUrl>\n")
            .append("     <name>").append(shape.poi!!.poiLocation!!.name).append("</name>\n")
            .append("      <LineString>\n")
        if (shape.isExtrude) command.append("       <extrude>1</extrude>\n")
        command.append("       <tessellate>1</tessellate>\n")
            .append("       <altitudeMode>absolute</altitudeMode>\n")
            .append("       <coordinates>\n")
        val points = shape.points
        val pointsLength = points!!.size
        var point: Point
        for (i in 0 until pointsLength) {
            point = points[i] as Point
            command.append("        ").append(point.longitude).append(",").append(point.latitude)
                .append(",").append(point.altitude).append("\n")
        }
        command.append("       </coordinates>\n")
            .append("      </LineString>\n")
            .append("     </Placemark>\n\n")
        Log.w(TAG_DEBUG, "SHAPE COMMAND: $command")
        folderBalloonShapes.append(command.toString())
        return animate
    }

    private fun commandWait(duration: Int): String {
        val waitCommand = """    <gx:Wait>
     <gx:duration>$duration</gx:duration>
    </gx:Wait>

"""
        Log.w(TAG_DEBUG, "WAIT COMMAND:$waitCommand")
        return waitCommand
    }

    @JvmStatic
    fun buildCommandwriteStartTourFile(): String {
        val command = "echo \"http://lg1:81/Tour.kml\"  > " +
                BASE_PATH +
                "kmls.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    @JvmStatic
    fun buildCommandStartTour(): String {
        val command = "echo \"playtour=TestTour\" > /tmp/query.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    @JvmStatic
    fun buildCommandExitTour(): String {
        val command = "echo \"exittour=true\" > /tmp/query.txt"
        Log.w(TAG_DEBUG, "command: $command")
        return command
    }

    @JvmStatic
    fun buildCommandCleanSlaves(): String {
        val blank = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
        "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">"+
        "<Document>"+
        "</Document>"+
        "</kml>"
        val command = "echo '"+blank+"' > " + BASE_PATH + "kml/slave_2.kml"
        Log.w(TAG_DEBUG, "commandCleanSlaves: $command")
        return command
    }
    fun buildRebootSlaves(password:String,i:Int):String{
        val command="sshpass -p $password ssh -t lg$i \"echo $password | sudo -S reboot\""
        Log.w(TAG_DEBUG, "commandRebootSlaves: $command")
        return command
    }
    fun buildCleanLogo():String{
        val blank="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
        "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">"+
        "<Document id=\"slave_3\">"+
        "</Document>"+
        "</kml>"
        val command = "echo '"+blank+"' > " + BASE_PATH + "kml/slave_3.kml"
        Log.w(TAG_DEBUG, "commandCleanLogo: $command")
        return command

    }

}