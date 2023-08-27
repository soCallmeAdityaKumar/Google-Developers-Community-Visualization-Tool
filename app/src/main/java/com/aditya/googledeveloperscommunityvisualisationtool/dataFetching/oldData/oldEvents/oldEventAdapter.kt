package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Event
import com.aditya.googledeveloperscommunityvisualisationtool.R

class oldEventAdapter(var eventList:List<Event>):RecyclerView.Adapter<oldEventAdapter.myViewHolder>() {
    class myViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val eventName=itemView.findViewById<TextView>(R.id.title)
        val cityname=itemView.findViewById<TextView>(R.id.date)
        val countryname=itemView.findViewById<TextView>(R.id.description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.pasteventlist,parent,false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.eventName.text=eventList[position].name
        holder.cityname.text=eventList[position].group.name
        holder.countryname.text=eventList[position].local_date
        holder.countryname.maxLines=3
        holder.eventName.setSelected(true)
        holder.cityname.setSelected(true)
        holder.countryname.setSelected(true)

    }
    fun refreshdata(eventlist:List<Event>){
        this.eventList=eventlist
        notifyDataSetChanged()
    }
}