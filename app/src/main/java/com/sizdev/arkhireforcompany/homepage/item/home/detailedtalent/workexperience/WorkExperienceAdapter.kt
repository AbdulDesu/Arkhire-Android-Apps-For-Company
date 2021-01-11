package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.workexperience

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemTalentWorkExperienceBinding

class WorkExperienceAdapter : RecyclerView.Adapter<WorkExperienceAdapter.WorkExperienceHolder>() {
    private var items = mutableListOf<WorkExperienceModel>()

    fun addList(list: List<WorkExperienceModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class WorkExperienceHolder(val binding: ItemTalentWorkExperienceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkExperienceHolder {
        return WorkExperienceHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_talent_work_experience, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WorkExperienceHolder, position: Int) {
        val item = items[position]
        holder.binding.tvWorkExperiencePosition.text = item.experienceTitle
        holder.binding.tvWorkExperienceCompany.text = item.experienceSource
        holder.binding.tvWorkExperienceYearStart.text = "${item.experienceStart} -"
        holder.binding.tvWorkExperienceYearEnd.text = " ${item.experienceEnd}"

        when (item.experienceDesc){
            "" -> holder.binding.tvWorkExperienceDescription.text = "-"
            else -> holder.binding.tvWorkExperienceDescription.text = item.experienceDesc
        }

        holder.itemView.setOnClickListener {
            val context = holder.binding.itemWorkexperienceHolder.context
            val intent = Intent(context, WorkExperienceDetailActivity::class.java)

            intent.putExtra("experienceID", item.experienceID)
            intent.putExtra("experienceTitle", item.experienceTitle)
            intent.putExtra("experienceSource", item.experienceSource)
            intent.putExtra("experienceStart", item.experienceStart)
            intent.putExtra("experienceEnd", item.experienceEnd)
            intent.putExtra("experienceDesc", item.experienceDesc)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}