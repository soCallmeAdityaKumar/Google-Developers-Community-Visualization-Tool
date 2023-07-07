package com.example.googledeveloperscommunityvisualisationtool.fragments.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.databinding.PieChartItemBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.GdgChapterCompleteDetails.ChapterEntity

class PieChartAdapter(var countryCount:MutableList<CountryCountData>):RecyclerView.Adapter<PieChartAdapter.pieChartViewHolder>() {
    class pieChartViewHolder(val binding:PieChartItemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pieChartViewHolder {
        val view=PieChartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  pieChartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryCount.size
    }

    override fun onBindViewHolder(holder: pieChartViewHolder, position: Int) {
        with(holder){
            with(countryCount[position]){
                binding.colorView.setBackgroundColor( Color.parseColor(this.color))
                binding.countryname.text=this.countryName
                binding.count.text=this.count.toString()
            }
        }
    }
    fun refreshData(countlist:MutableList<CountryCountData>){
        this.countryCount=countlist
        notifyDataSetChanged()

    }


}