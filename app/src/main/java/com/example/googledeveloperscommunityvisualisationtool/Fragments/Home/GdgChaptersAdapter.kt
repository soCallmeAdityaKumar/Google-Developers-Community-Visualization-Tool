package com.example.googledeveloperscommunityvisualisationtool.Fragments.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.databinding.GdgChaptersListBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity

class GdgChaptersAdapter(var chapterList: List<ChapterEntity>):RecyclerView.Adapter<GdgChaptersAdapter.MyViewHolder>() {
    class MyViewHolder(val binding:GdgChaptersListBinding):RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= GdgChaptersListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(chapterList[position]){
                binding.GdgNameTextView.text=this.gdgName
                binding.GdgCityTextView.text=this.city_name
            }
        }
    }
    fun refreshData(chapterList:List<ChapterEntity>){
        this.chapterList=chapterList
        notifyDataSetChanged()

    }
}

