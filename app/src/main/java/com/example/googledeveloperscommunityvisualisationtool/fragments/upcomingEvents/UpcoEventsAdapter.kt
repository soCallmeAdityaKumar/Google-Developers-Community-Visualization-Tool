package com.example.googledeveloperscommunityvisualisationtool.fragments.upcomingEvents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.dataClass.volley.Result
import com.example.googledeveloperscommunityvisualisationtool.databinding.EventsitemlistBinding

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
                binding.GDGname.text=this.chapter?.title.toString()
                binding.city.text=this.city.toString()
                var tag=""
                for (i in 0 until this.tags!!.size){
                    tag += this.tags[i]
                }
//                binding.tags.text=tag.toString()

                }
            }
        }
    fun refreshData(eventsList:List<Result>){
        this.eventList=eventsList
        notifyDataSetChanged()

    }

}

