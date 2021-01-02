package com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R

class FullStackWebAdapter : RecyclerView.Adapter<FullStackWebAdapter.FullStackWebTalentHolder>() {

    val listName = listOf("Kiryuu Sento", "Sento Kiryuu", "Sento Sento", "Kiryuu Kiryuu")

    class FullStackWebTalentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_name:TextView = itemView.findViewById(R.id.tv_fullStackWebTalentName)
        var tv_title: TextView = itemView.findViewById(R.id.tv_fullStackWebTalentTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FullStackWebTalentHolder {
        return FullStackWebTalentHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fullstack_web_talent, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FullStackWebTalentHolder, position: Int) {
        holder.tv_name.text = listName[position]
        holder.tv_title.text = "FullStack Web"
    }

    override fun getItemCount(): Int = listName.size
}