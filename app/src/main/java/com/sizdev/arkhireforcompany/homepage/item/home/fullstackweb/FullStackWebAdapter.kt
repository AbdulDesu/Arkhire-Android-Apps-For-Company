package com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemTalentAtHomepageBinding
import com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.TalentProfileActivity
import com.squareup.picasso.Picasso

class FullStackWebAdapter : RecyclerView.Adapter<FullStackWebAdapter.FullStackWebTalentHolder>() {
    private var items = mutableListOf<FullStackWebModel>()

    fun addList(list: List<FullStackWebModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class FullStackWebTalentHolder(val binding: ItemTalentAtHomepageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FullStackWebTalentHolder {
        return FullStackWebAdapter.FullStackWebTalentHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_talent_at_homepage,
                parent, false
            )
        )}

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FullStackWebTalentHolder, position: Int) {
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

            intent.putExtra("talentID", item.talentID)
            intent.putExtra("accountID", item.accountID)
            intent.putExtra("talentName", item.accountName)
            intent.putExtra("talentTitle", item.talentTitle)
            intent.putExtra("talentImage", item.talentImage)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}