package com.example.googledeveloperscommunityvisualisationtool.fragments.Notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googledeveloperscommunityvisualisationtool.dataClass.volley.Result
import com.example.googledeveloperscommunityvisualisationtool.databinding.FragmentNotificationBinding
import com.example.googledeveloperscommunityvisualisationtool.databinding.NotificationItemBinding
import com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails.pastEventAdapter

class NotifyAdapter(var list:ArrayList<notificationData>):RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> (){
    class NotifyViewHolder(val binding:NotificationItemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        val binding=NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotifyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.title.text=this.title
                binding.timeText.text=((System.currentTimeMillis()-this.time)/60000).toInt().toString()+"min ago"
                Glide.with(binding.root)
                    .load(this.image)
                    .centerCrop()
                    .into(binding.logoNotify)

            }
        }
    }
    fun refreshData(notification:ArrayList<notificationData>){
        this.list=notification
        notifyDataSetChanged()

    }
}