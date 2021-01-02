package com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R

class FullStackMobileAdapter : RecyclerView.Adapter<FullStackMobileAdapter.FullStackMobileTalentHolder>() {

    val listName = listOf("Kiryuu Sento", "Sento Kiryuu", "Sento Sento", "Kiryuu Kiryuu")

    class FullStackMobileTalentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_name:TextView = itemView.findViewById(R.id.tv_fullStackMobileTalentName)
        var tv_title: TextView = itemView.findViewById(R.id.tv_fullStackMobileTalentTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FullStackMobileTalentHolder {
        return FullStackMobileTalentHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fullstack_mobile_talent, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FullStackMobileTalentHolder, position: Int) {
        holder.tv_name.text = listName[position]
        holder.tv_title.text = "FullStack Mobile"
    }

    override fun getItemCount(): Int = listName.size
}