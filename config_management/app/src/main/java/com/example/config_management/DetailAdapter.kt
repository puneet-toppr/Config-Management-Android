package com.example.config_management

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_domain_feature.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class DetailAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var myDataset: List<DomainFeatureModel> = listOf()

    inner class DetailViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        fun bind (data: DomainFeatureModel){
            view.list.text = data.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_domain_feature, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailAdapter.DetailViewHolder) {
            holder.bind(myDataset[position])
        }
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    fun setDetailData(list: List<DomainFeatureModel>) {
        this.myDataset = list
        notifyDataSetChanged()
    }

}