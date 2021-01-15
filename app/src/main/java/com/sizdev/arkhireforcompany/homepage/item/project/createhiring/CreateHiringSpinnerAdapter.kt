package com.sizdev.arkhireforcompany.homepage.item.project.createhiring

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ItemSpinnerProjectBinding
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectModel

class CreateHiringSpinnerAdapter (private var context: Context) : BaseAdapter() {
    private lateinit var binding: ItemSpinnerProjectBinding
    private var items = mutableListOf<ProjectModel>()

    fun addList(list: List<ProjectModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): Any {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, parent: ViewGroup): View {
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            binding = DataBindingUtil.inflate(inflater, R.layout.item_spinner_project, parent, false)
        }

        binding.tvProjectName.text = items[i].projectTitle

        return binding.root
    }
}