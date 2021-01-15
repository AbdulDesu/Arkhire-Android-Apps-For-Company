package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemHiringListBinding
import com.squareup.picasso.Picasso

class ProjectHiringAdapter : RecyclerView.Adapter<ProjectHiringAdapter.HiringListHolder>() {
    private var items = mutableListOf<ProjectHiringModel>()

    fun addList(list: List<ProjectHiringModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class HiringListHolder(val binding: ItemHiringListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiringListHolder {
        return HiringListHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_hiring_list, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HiringListHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTalentName.text = item.talentName
        holder.binding.tvTalentTitle.text = item.talentTitle
        holder.binding.tvTalentSalary.text = item.projectSalary

        when(item.talentImage){
            null -> holder.binding.ivTalentImage.setImageResource(R.drawable.ic_empty_image)
            else -> {
                Picasso.get()
                        .load("http://54.82.81.23:911/image/${item.talentImage}")
                        .resize(512, 512)
                        .centerCrop()
                        .into(holder.binding.ivTalentImage)
            }
        }

        when(item.hiringStatus){
            "Approved" -> holder.binding.ivHiringStatus.setImageResource(R.drawable.ic_approved)
            "Declined" -> holder.binding.ivHiringStatus.setImageResource(R.drawable.ic_declined)
            else -> holder.binding.ivHiringStatus.setImageResource(R.drawable.ic_waiting)
        }
    }

    override fun getItemCount(): Int = items.size
}