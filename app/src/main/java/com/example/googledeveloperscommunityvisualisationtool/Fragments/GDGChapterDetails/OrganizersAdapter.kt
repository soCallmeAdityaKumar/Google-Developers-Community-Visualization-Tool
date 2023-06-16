package com.example.googledeveloperscommunityvisualisationtool.Fragments.GDGChapterDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.GDGDetails
import com.example.googledeveloperscommunityvisualisationtool.Fragments.Home.Organizers
import com.example.googledeveloperscommunityvisualisationtool.databinding.GdgorganiserlistBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity

class OrganizersAdapter(var organizers:List<Organizers>):RecyclerView.Adapter<OrganizersAdapter.myViewHolder>() {
    class myViewHolder( val binding:GdgorganiserlistBinding) :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding=GdgorganiserlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return myViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return organizers.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        with(holder){
            with(organizers[position]){
                binding.organizername.text=this.organizername
                binding.organizertitle.text=this.organizerTitle
                binding.organizercompany.text=this.organizercompany
                Glide.with(binding.root)
                    .load(this.organizerimage)
                    .centerCrop()
                    .into(binding.image)
            }
        }
    }
    fun refreshData(organizerList:List<Organizers>){
        this.organizers=organizerList
        notifyDataSetChanged()

    }
}