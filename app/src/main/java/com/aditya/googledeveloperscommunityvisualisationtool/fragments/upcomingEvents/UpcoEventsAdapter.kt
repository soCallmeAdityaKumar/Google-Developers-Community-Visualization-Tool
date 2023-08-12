package com.aditya.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.dataClass.volley.Result
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.EventsitemlistBinding
import com.aditya.googledeveloperscommunityvisualisationtool.fragments.Calendar.CalendarFragment
import kotlin.random.Random

class UpcoEventsAdapter(private var eventList:List<Result>):RecyclerView.Adapter<UpcoEventsAdapter.MyViewHolder>() {
    private lateinit var mListener:onItemClickListener
    interface  onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener=listener
    }
    class MyViewHolder(val binding: EventsitemlistBinding,listener: onItemClickListener) :RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =EventsitemlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding,mListener)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(eventList[position]){
                binding.eventTitle.text=this.title.toString()
                binding.GDGname.text=this.chapter.title
                binding.city.text=this.city.toString()
                if(this.picture.thumbnail_url!!.isNotEmpty()){
                    Glide.with(binding.root)
                        .load(this.picture.thumbnail_url)
                        .centerCrop()
                        .into(binding.eventLogo)
                }else{
                    Glide.with(binding.root)
                        .load(R.drawable.gdglogo)
                        .centerCrop()
                        .into(binding.eventLogo)
                }

                var tag=""
                for (i in 0 until this.tags!!.size){
                    tag += this.tags[i]
                }
                binding.eventTitle.setSelected(true)
                binding.GDGname.setFocusable(true)
                binding.city.setFocusable(true)

                }
            }
        }

    fun refreshData(eventsList:List<Result>){
        this.eventList=eventsList
        notifyDataSetChanged()

    }

}

