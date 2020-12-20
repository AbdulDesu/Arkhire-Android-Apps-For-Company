package com.sizdev.arkhirefortalent.homepage.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R

class WorkExperienceAdapter : RecyclerView.Adapter<WorkExperienceAdapter.TalentWorkExperienceHolder>() {

    val listPosition = listOf("Software Engineer", "Senior Software Engineer", "Software developer", "Junior Developer", "Team Leader", "Software Testing Officer")
    val listYear = listOf("2014-2015","2015-2016", "2016-2017", "2017-2018", "2018-2019", "2019-2020").reversed()

    class TalentWorkExperienceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var positionExperience: TextView = itemView.findViewById(R.id.tv_workExperiencePosition)
        var yearExperience: TextView = itemView.findViewById(R.id.tv_workExperienceYearRange)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalentWorkExperienceHolder {
        return TalentWorkExperienceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_talent_work_experience, parent, false))
    }

    override fun onBindViewHolder(holder: TalentWorkExperienceHolder, position: Int) {
        holder.positionExperience.text = listPosition[position]
        holder.yearExperience.text = listYear[position]
    }

    override fun getItemCount(): Int = listPosition.size


}