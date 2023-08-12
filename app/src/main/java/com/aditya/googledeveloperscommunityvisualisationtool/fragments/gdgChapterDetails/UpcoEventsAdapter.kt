package com.aditya.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.UpcomeventlistBinding

class UpcoEventsAdapter(var Events:List<events>):RecyclerView.Adapter<UpcoEventsAdapter.myViewHolder>() {
    private lateinit var Listene:onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        Listene=listener
    }
    class myViewHolder(val binding: UpcomeventlistBinding,listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.bookticketbutton.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding =
            UpcomeventlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myViewHolder(binding,Listene)
    }

    override fun getItemCount(): Int {
        return Events.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        with(holder) {
            with(Events[position]) {
                binding.title.text = this.title
                binding.date.text = this.date
            }
            binding.date.setSelected(true)
            binding.title.setSelected(true)


        }

    }

    fun refreshData(eventList: List<events>) {
        this.Events = eventList
        notifyDataSetChanged()

    }
}