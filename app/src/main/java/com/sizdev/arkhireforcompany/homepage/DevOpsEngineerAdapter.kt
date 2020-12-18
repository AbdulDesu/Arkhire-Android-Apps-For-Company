package com.sizdev.arkhireforcompany.homepage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import kotlinx.android.synthetic.main.item_devops_engineer_talent.view.*

class DevOpsEngineerAdapter : RecyclerView.Adapter<DevOpsEngineerAdapter.DevopsEngineerTalentHolder>() {

    val listName = listOf("Kiryuu Sento", "Sento Kiryuu", "Sento Sento", "Kiryuu Kiryuu")

    class DevopsEngineerTalentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_name: TextView = itemView.findViewById(R.id.tv_devOpsEngineerTalentName)
        var tv_title: TextView = itemView.findViewById(R.id.tv_devOpsEngineerTalentTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevopsEngineerTalentHolder {
        return DevOpsEngineerAdapter.DevopsEngineerTalentHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_devops_engineer_talent, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DevopsEngineerTalentHolder, position: Int) {
        holder.tv_name.text = listName[position]
        holder.tv_title.text = "DevOps Engineer"
    }

    override fun getItemCount(): Int = listName.size
}