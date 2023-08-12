package com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aditya.googledeveloperscommunityvisualisationtool.dataFetching.oldData.oldEvents.oldEventsDataClass.Organizer
import com.aditya.googledeveloperscommunityvisualisationtool.R

class oldGdgOrganAdap(var organizerList:List<Organizer>):RecyclerView.Adapter<oldGdgOrganAdap.myViewHolder>() {
    class myViewHolder( itemView: View) :RecyclerView.ViewHolder(itemView) {
        val image=itemView.findViewById<ImageView>(R.id.image)
        val name=itemView.findViewById<TextView>(R.id.organizername)
        val country=itemView.findViewById<TextView>(R.id.organizercompany)
        val role=itemView.findViewById<TextView>(R.id.organizertitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.gdgorganiserlist,parent,false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return organizerList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.name.text=organizerList[position].name
        holder.country.text=organizerList[position].country
        holder.role.text=organizerList[position].group_profile.role
        Glide.with(holder.itemView)
            .load(organizerList[position].photo.photo_link)
            .centerCrop()
            .into(holder.image)
    }
    fun refreshdata(organizer:List<Organizer>){
        this.organizerList=organizer
        notifyDataSetChanged()
    }
}