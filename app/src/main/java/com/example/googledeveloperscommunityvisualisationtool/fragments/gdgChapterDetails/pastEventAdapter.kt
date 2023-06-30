package com.example.googledeveloperscommunityvisualisationtool.fragments.gdgChapterDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.databinding.PasteventlistBinding

class pastEventAdapter(var Events:List<events>):RecyclerView.Adapter<pastEventAdapter.myViewHolder>() {

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
                binding.title.text = this.title
                binding.date.text = this.date
                binding.description.text=this.typeORdescription
            }
        }
    }

    fun refreshData(eventList: List<events>) {
        this.Events = eventList
        notifyDataSetChanged()

    }
}