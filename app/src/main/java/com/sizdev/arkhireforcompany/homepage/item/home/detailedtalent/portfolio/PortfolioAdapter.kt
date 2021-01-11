package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.portfolio

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemPortfolioBinding

import com.squareup.picasso.Picasso

class PortfolioAdapter : RecyclerView.Adapter<PortfolioAdapter.ShowPortfolioHolder>() {
    private var items = mutableListOf<PortfolioModel>()

    fun addList(list: List<PortfolioModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ShowPortfolioHolder(val binding: ItemPortfolioBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowPortfolioHolder {
        return ShowPortfolioHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_portfolio, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShowPortfolioHolder, position: Int) {
        val item = items[position]
        when (item.portfolioImage) {
            null -> holder.binding.ivPortfolio.setImageResource(R.drawable.ic_project_bg)
            else -> {
                Picasso.get()
                        .load("http://54.82.81.23:911/image/${item.portfolioImage}")
                        .resize(512, 512)
                        .centerCrop()
                        .into(holder.binding.ivPortfolio)
            }
        }

        holder.itemView.setOnClickListener {
            val context = holder.binding.itemPortfolioHolder.context
            val intent = Intent(context, PortfolioDetailsActivity::class.java)

            intent.putExtra("portfolioID", item.portfolioID)
            intent.putExtra("portfolioTitle", item.portfolioTitle)
            intent.putExtra("portfolioDesc", item.portfolioDesc)
            intent.putExtra("portfolioImage", item.portfolioImage)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}