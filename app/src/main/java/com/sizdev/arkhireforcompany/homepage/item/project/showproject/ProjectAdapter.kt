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
        holder.binding.tvProjectTitle.text = item.projectTitle
        holder.binding.tvProjectSalary.text = item.projectSallary
        holder.binding.tvProjectDuration.text = "In ${item.projectDuration}"
        when(item.hiringStatus){
            "Approved" -> holder.binding.ivProjectStatus.setImageResource(R.drawable.ic_approved)
            "Declined" -> holder.binding.ivProjectStatus.setImageResource(R.drawable.ic_declined)
            else -> holder.binding.ivProjectStatus.setImageResource(R.drawable.ic_waiting)
        }

        holder.itemView.setOnClickListener {
            val context = holder.binding.itemListProjectHolder.context
            val intent = Intent(context, ProjectDetailActivity::class.java)
            val projectID = item.projectID.toString()
            val projectTitle = item.projectTitle.toString()
            val projectSalary = item.projectSallary.toString()
            val projectDesc = item.projectDesc.toString()
            val projectDuration = item.projectDuration.toString()
            val projectStatus = item.hiringStatus.toString()
            val msgReply = item.replyMsg.toString()
            val repliedAt = item.repliedAt.toString()

            intent.putExtra("projectID", projectID)
            intent.putExtra("projectTitle", projectTitle)
            intent.putExtra("projectSalary", projectSalary)
            intent.putExtra("projectDesc", projectDesc)
            intent.putExtra("projectDuration", projectDuration)
            intent.putExtra("projectStatus", projectStatus)
            intent.putExtra("msgReply", msgReply)
            intent.putExtra("repliedAt", repliedAt)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}