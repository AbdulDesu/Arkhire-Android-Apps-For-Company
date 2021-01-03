package com.sizdev.arkhireforcompany.homepage.item.account.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R

class CompanyLookingForAdapter : RecyclerView.Adapter<CompanyLookingForAdapter.CompanyLookingForHolder>() {

    val listInterest = listOf("Android Developer", "Devops Engineer", "Fullstack Mobile", "Fullstack web")

    class CompanyLookingForHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_lookingfor:TextView = itemView.findViewById(R.id.iv_lookingFor)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyLookingForHolder {
        return CompanyLookingForHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_looking_for, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CompanyLookingForHolder, position: Int) {
        holder.tv_lookingfor.text = listInterest[position]
    }

    override fun getItemCount(): Int = listInterest.size
}