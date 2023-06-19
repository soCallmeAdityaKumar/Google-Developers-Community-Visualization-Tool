package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldGdg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledeveloperscommunityvisualisationtool.databinding.OldgdglistitemBinding
import com.example.googledeveloperscommunityvisualisationtool.roomdatabase.OldData.OldGDGEntity

class oldgdgAdapter(var gdglist:List<OldGDGEntity>): RecyclerView.Adapter<oldgdgAdapter.myViewHolder>() {

    private lateinit var mListener:onItemClickListener
    interface  onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: oldgdgAdapter.onItemClickListener){
        mListener=listener
    }
    class myViewHolder(val binding:OldgdglistitemBinding, listener: onItemClickListener):RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding=OldgdglistitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  myViewHolder(binding,mListener)
    }

    override fun getItemCount(): Int {
        return gdglist.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        with(holder){
            with(gdglist[position]){
                binding.city.text=this.city.toString()
                binding.gdgname.text=this.name.toString()
                binding.country.text=this.country.toString()
            }
        }
    }
    fun refreshdata(list:List<OldGDGEntity>){
        this.gdglist=list
        notifyDataSetChanged()
    }
}
