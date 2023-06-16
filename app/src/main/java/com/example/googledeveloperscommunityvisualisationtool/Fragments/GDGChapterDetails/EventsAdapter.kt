package com.example.googledeveloperscommunityvisualisationtool.Fragments.GDGChapterDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.databinding.PasteventlistBinding

class EventsAdapter(var Events:List<events>):RecyclerView.Adapter<EventsAdapter.myViewHolder>() {
    class myViewHolder(val binding: PasteventlistBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding =
            PasteventlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return Events.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        with(holder) {
            with(Events[position]) {
                binding.title.text = this.title.toString()
                binding.date.text = this.date.toString()
                binding.description.text = this.typeORdescription.toString()
            }
        }
    }

    fun refreshData(eventList: List<events>) {
        this.Events = eventList
        notifyDataSetChanged()

    }
}