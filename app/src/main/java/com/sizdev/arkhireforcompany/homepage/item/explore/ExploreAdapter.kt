package com.sizdev.arkhireforcompany.homepage.item.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
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
        holder.binding.tvTalentLocation.text = item.talentCity

        when (item.talentTitle){
            null -> holder.binding.showAllTalentItem.visibility = View.GONE
            else -> {
                holder.binding.showAllTalentItem.visibility = View.VISIBLE
                holder.binding.tvTalentTitle.text = item.talentTitle
            }
        }

        when (item.talentSkill1){
            null -> holder.binding.tvTalentSkill1.visibility = View.INVISIBLE
            else -> {
                holder.binding.tvTalentSkill1.visibility = View.VISIBLE
                holder.binding.tvTalentSkill1.text = item.talentSkill1
            }
        }

        when (item.talentSkill1){
            null -> holder.binding.tvTalentSkill2.visibility = View.INVISIBLE
            else -> {
                holder.binding.tvTalentSkill2.visibility = View.VISIBLE
                holder.binding.tvTalentSkill2.text = item.talentSkill2
            }
        }

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

        holder.itemView.setOnClickListener {
            val item = items[position]
            val context = holder.binding.showAllTalentItem.context
            val intent = Intent(context, TalentProfileActivity::class.java)

            intent.putExtra("talentID", item.talentID)
            intent.putExtra("accountID", item.accountID)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}