package com.sizdev.arkhireforcompany.homepage.item.project.showproject

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemListProjectBinding
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.ProjectDetailActivity
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class ProjectAdapter : RecyclerView.Adapter<ProjectAdapter.showProjectList>() {
    private var items = mutableListOf<ProjectModel>()

    fun addList(list: List<ProjectModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class showProjectList(val binding: ItemListProjectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): showProjectList {
        return showProjectList(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_project, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: showProjectList, position: Int) {
        val item = items[position]
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        // Timestamp Convert
        val dateSplitter = item.postedAt?.split("T")
        val timeSplitter = dateSplitter?.get(1)?.split(".")

        holder.binding.tvProjectTitle.text = item.projectTitle
        if(item.projectSalary.isNullOrEmpty()){
            holder.binding.tvProjectSalary.text = "Data Malformed"
        }
        else{
            holder.binding.tvProjectSalary.text = format.format(item.projectSalary?.toDouble())
        }

        holder.binding.tvProjectDuration.text = "Deadline At: ${item.projectDuration}"
        holder.binding.tvProjectCreated.text = "${dateSplitter!![0]} - ${timeSplitter!![0]}"

        Picasso.get()
                .load("http://54.82.81.23:911/image/${item.projectImage}")
                .resize(1280, 560)
                .centerCrop()
                .into(holder.binding.ivProjectImage)

        holder.itemView.setOnClickListener {
            val context = holder.binding.itemListProjectHolder.context
            val intent = Intent(context, ProjectDetailActivity::class.java)

            intent.putExtra("projectID", item.projectID)
            intent.putExtra("projectTitle", item.projectTitle)
            intent.putExtra("projectSalary", item.projectSalary)
            intent.putExtra("projectDesc", item.projectDesc)
            intent.putExtra("projectDuration", item.projectDuration)
            intent.putExtra("projectImage", item.projectImage)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}