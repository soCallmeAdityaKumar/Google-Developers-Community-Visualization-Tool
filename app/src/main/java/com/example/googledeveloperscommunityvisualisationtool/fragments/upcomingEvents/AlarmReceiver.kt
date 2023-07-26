package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.googledeveloperscommunityvisualisationtool.MainActivity
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyEntity
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModel
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.notificationRoom.NotifyViewModelFactory

class AlarmReceiver:BroadcastReceiver() {
    private  val CHANNEL_ID="Upcoming_Event_Notification"
    lateinit var notificationViewModel:NotifyViewModel
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?){

        notificationViewModel= NotifyViewModel(context!!)
        val i=Intent(context,MainActivity::class.java)
        var title:String=intent?.getStringExtra("title")!!
        var image:String=intent?.getStringExtra("image")!!
        var desc:String=intent?.getStringExtra("desc")!!
        intent!!.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent=PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)

        val builder=NotificationCompat.Builder(context!!,CHANNEL_ID)
            .setSmallIcon(R.drawable.googledeveloperscommunitylogo)
            .setContentTitle("Hey! You have an Event right now")
            .setContentText(title)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        storeNotificationPref(title,image,desc)

        val notificationManager=NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())
    }

    private fun storeNotificationPref(title:String,image:String,desc:String) {
      notificationViewModel.addNotificationViewModel(NotifyEntity(0,title,desc,System.currentTimeMillis(),image))
    }
}