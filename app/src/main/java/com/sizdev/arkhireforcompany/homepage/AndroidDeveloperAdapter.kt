package com.sizdev.arkhireforcompany.homepage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R

class AndroidDeveloperAdapter : RecyclerView.Adapter<AndroidDeveloperAdapter.AndroidDeveloperTalentHolder>() {

    val listName = listOf("Kiryuu Sento", "Sento Kiryuu", "Sento Sento", "Kiryuu Kiryuu")

    class AndroidDeveloperTalentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_name:TextView = itemView.findViewById(R.id.tv_androidDeveloperTalentName)
        var tv_title: TextView = itemView.findViewById(R.id.tv_androidDeveloperTalentTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AndroidDeveloperTalentHolder {
        return AndroidDeveloperTalentHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_android_developer_talent, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AndroidDeveloperTalentHolder, position: Int) {
        holder.tv_name.text = listName[position]
        holder.tv_title.text = "Android Developer"
    }

    override fun getItemCount(): Int = listName.size
}