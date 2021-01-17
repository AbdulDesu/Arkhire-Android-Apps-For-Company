package com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemTalentAtHomepageBinding
import com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.TalentProfileActivity
import com.squareup.picasso.Picasso

class FullStackMobileAdapter : RecyclerView.Adapter<FullStackMobileAdapter.FullStackMobileTalentHolder>() {
    private var items = mutableListOf<FullStackMobileModel>()

    fun addList(list: List<FullStackMobileModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class FullStackMobileTalentHolder(val binding: ItemTalentAtHomepageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FullStackMobileTalentHolder {
        return FullStackMobileAdapter.FullStackMobileTalentHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_talent_at_homepage,
                parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FullStackMobileTalentHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTalentName.text = item.accountName
        holder.binding.tvTalentTitle.text = item.talentTitle
        holder.binding.tvTalentWorkTime.text = item.talentTime
        holder.binding.talentSkill1.text = item.talentSkill1
        holder.binding.talentSkill2.text = item.talentSkill2
        holder.binding.talentSkill3.text = item.talentSkill3
        Picasso.get()
                .load("http://54.82.81.23:911/image/${item.talentImage}")
                .resize(512, 512)
                .centerCrop()
                .into(holder.binding.ivTalentImage)

        holder.itemView.setOnClickListener {
            val context = holder.binding.itemTalentHolder.context
            val intent = Intent(context, TalentProfileActivity::class.java)
            val talentID = item.talentID.toString()
            val talentName = item.accountName.toString()
            val talentTitle = item.talentTitle.toString()
            val talentImage = item.talentImage.toString()

            intent.putExtra("talentID", talentID)
            intent.putExtra("talentName", talentName)
            intent.putExtra("talentTitle", talentTitle)
            intent.putExtra("talentImage", talentImage)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}