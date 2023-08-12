package com.aditya.googledeveloperscommunityvisualisationtool.fragments.Notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.bumptech.glide.Glide
import com.aditya.googledeveloperscommunityvisualisationtool.dataClass.volley.Result
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.NotificationItemBinding
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.pastEventAdapter
import com.google.api.client.util.DateTime
import kotlin.time.Duration.Companion.hours

class NotifyAdapter(var list:ArrayList<notificationData>):RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> (){
    class NotifyViewHolder(val binding: NotificationItemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        val binding=
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotifyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.title.text=this.title
                var diffTime=(System.currentTimeMillis()-this.timeInMiliSec)/60000
                if(diffTime<60){
                    binding.timeText.text=(diffTime.toString())+" min ago"
                }
                else if(diffTime>=60){
                    diffTime/=60
                    binding.timeText.text=(diffTime.toString())+" hour ago"
                }
                else{
                    binding.timeText.text="Long time Ago"
                }
                binding.StartsInText.text="Start on "+this.time
                if(this.image.isNotEmpty()) {
                    Glide.with(binding.root)
                        .load(this.image)
                        .centerCrop()
                        .into(binding.logoNotify)
                }else{
                    Glide.with(binding.root)
                        .load(R.drawable.gdglogo)
                        .centerCrop()
                        .into(binding.logoNotify)
                }
            }
        }
    }
    fun refreshData(notification:ArrayList<notificationData>){
        this.list=notification
        notifyDataSetChanged()

    }
}