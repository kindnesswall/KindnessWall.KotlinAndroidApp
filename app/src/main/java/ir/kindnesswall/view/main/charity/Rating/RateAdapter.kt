package ir.kindnesswall.view.main.charity.Rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.model.RateModel
import ir.kindnesswall.databinding.OpinionBinding

class RateAdapter(val list :ArrayList<RateModel>) : RecyclerView.Adapter<RateAdapter.RateAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateAdapterViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : OpinionBinding = DataBindingUtil.inflate(layoutInflater , R.layout.opinion,parent,false)
        return RateAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RateAdapterViewHolder, position: Int) {
        //holder.itemView = list.get(position)
       // holder.itemView.opin =

    }

    override fun getItemCount(): Int =list.count()

    inner class RateAdapterViewHolder(vieww: OpinionBinding) : RecyclerView.ViewHolder(vieww.root)

}