package com.sizdev.arkhireforcompany.homepage.item.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemAllTalentListBinding
import com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.TalentProfileActivity
import com.squareup.picasso.Picasso

class ExploreAdapter : RecyclerView.Adapter<ExploreAdapter.ShowAllTalentHolder>() {
    private var items = mutableListOf<ExploreModel>()

    fun addList(list: List<ExploreModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ShowAllTalentHolder(val binding: ItemAllTalentListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowAllTalentHolder {
        return ShowAllTalentHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_all_talent_list, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShowAllTalentHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTalentName.text = item.accountName
        holder.binding.tvTalentTitle.text = item.talentTitle
        holder.binding.tvTalentLocation.text = item.talentCity
        holder.binding.tvTalentSkill1.text = item.talentSkill1
        holder.binding.tvTalentSkill2.text = item.talentSkill2
        Picasso.get()
            .load("http://54.82.81.23:911/image/${item.talentImage}")
            .resize(86, 86)
            .centerCrop()
            .into(holder.binding.ivTalentImage)

        holder.itemView.setOnClickListener {
            val item = items[position]
            val context = holder.binding.showAllTalentItem.context
            val intent = Intent(context, TalentProfileActivity::class.java)
            val talentID = item.talentID.toString()
            val accountID = item.accountID.toString()
            val talentName = item.accountName.toString()
            val talentEmail = item.accountEmail.toString()
            val talentPhone = item.accountPhone.toString()
            val talentTitle = item.talentTitle.toString()
            val talentTime = item.talentTime.toString()
            val talentLocation = item.talentCity.toString()
            val talentDesc = item.talentDesc.toString()
            val talentImage = item.talentImage.toString()
            val talentGithub = item.talentGithub.toString()
            val talentCv = item.talentCv.toString()
            val talentSkill1 = item.talentSkill1.toString()
            val talentSkill2 = item.talentSkill2.toString()
            val talentSkill3 = item.talentSkill3.toString()
            val talentSkill4 = item.talentSkill4.toString()
            val talentSkill5 = item.talentSkill5.toString()

            intent.putExtra("talentID", talentID)
            intent.putExtra("accountID", accountID)
            intent.putExtra("talentName", talentName)
            intent.putExtra("talentEmail", talentEmail)
            intent.putExtra("talentPhone", talentPhone)
            intent.putExtra("talentTitle", talentTitle)
            intent.putExtra("talentTime", talentTime)
            intent.putExtra("talentLocation", talentLocation)
            intent.putExtra("talentDesc", talentDesc)
            intent.putExtra("talentImage", talentImage)
            intent.putExtra("talentGithub", talentGithub)
            intent.putExtra("talentCv", talentCv)
            intent.putExtra("talentSkill1", talentSkill1)
            intent.putExtra("talentSkill2", talentSkill2)
            intent.putExtra("talentSkill3", talentSkill3)
            intent.putExtra("talentSkill4", talentSkill4)
            intent.putExtra("talentSkill5", talentSkill5)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}