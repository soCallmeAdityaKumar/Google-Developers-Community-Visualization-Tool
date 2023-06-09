package com.example.googledeveloperscommunityvisualisationtool.Fragments.UpcomingEvents

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.DataClass.Volley.Result
import com.example.googledeveloperscommunityvisualisationtool.R
import com.example.googledeveloperscommunityvisualisationtool.databinding.EventsitemlistBinding

class Adapter(private var eventList:List<Result>):RecyclerView.Adapter<Adapter.MyViewHolder>() {
    class MyViewHolder(val binding: EventsitemlistBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=EventsitemlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(eventList[position]){
                binding.eventTitle.text=this.title.toString()
                binding.GDGname.text=this.chapter?.title.toString()
                binding.city.text=this.city.toString()
                var tag=""
                for (i in 0 until this.tags!!.size){
                    tag += this.tags[i]
                }
                binding.tags.text=tag.toString()

                }
            }
        }
    fun refreshData(eventsList:List<Result>){
        this.eventList=eventsList
        notifyDataSetChanged()

    }

}

