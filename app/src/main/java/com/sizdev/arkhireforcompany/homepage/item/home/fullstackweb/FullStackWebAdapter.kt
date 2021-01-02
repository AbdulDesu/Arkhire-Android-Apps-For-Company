package com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemFullstackWebTalentBinding
import com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.TalentProfileActivity
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileAdapter

class FullStackWebAdapter : RecyclerView.Adapter<FullStackWebAdapter.FullStackWebTalentHolder>() {
    private var items = mutableListOf<FullStackWebModel>()

    fun addList(list: List<FullStackWebModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class FullStackWebTalentHolder(val binding: ItemFullstackWebTalentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FullStackWebTalentHolder {
        return FullStackWebAdapter.FullStackWebTalentHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_fullstack_web_talent,
                parent, false
            )
        )}

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FullStackWebTalentHolder, position: Int) {
        val item = items[position]
        holder.binding.tvFullStackWebTalentName.text = item.accountName
        holder.binding.tvFullStackWebTalentTitle.text = item.talentTitle
        holder.binding.fullStackWebTalentSkill1.text = item.talentSkill1
        holder.binding.fullStackWebTalentSkill2.text = item.talentSkill2
        holder.binding.fullStackWebTalentSkill3.text = item.talentSkill3

        holder.itemView.setOnClickListener {
            val context = holder.binding.itemFullStackWebHolder.context
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