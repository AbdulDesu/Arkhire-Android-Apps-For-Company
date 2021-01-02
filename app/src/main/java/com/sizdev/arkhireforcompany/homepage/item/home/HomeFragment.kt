package com.sizdev.arkhireforcompany.homepage.item.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentHomeBinding
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebAdapter
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @SuppressLint("SimpleDateFormat", "WeekBasedYear", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // Get Date
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM YYYY")
        val currentDate = dateFormat.format(Date())

        // Get Saved name
        val sharedPrefData = requireActivity().getSharedPreferences("fullData", Context.MODE_PRIVATE)
        val savedData = sharedPrefData.getString("fullName", null)
        val nameSplitter = savedData?.split(" ")

        binding.tvHomeDate.text = currentDate
        if (nameSplitter?.size == 1){
            binding.tvUserGreeting.text = "Check This out, ${savedData?.capitalize()}"
        }
        else {
            val lastName = nameSplitter?.get(1).toString()
            binding.tvUserGreeting.text = "Check This out, ${lastName.capitalize()}"
        }

        binding.rvAndroidDeveloperTalent.adapter = AndroidDeveloperAdapter()
        binding.rvAndroidDeveloperTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        binding.rvDevOpsEngineerTalent.adapter = DevOpsEngineerAdapter()
        binding.rvDevOpsEngineerTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        binding.rvFullStackMobileTalent.adapter = FullStackMobileAdapter()
        binding.rvFullStackMobileTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        binding.rvFullStackWebTalent.adapter = FullStackWebAdapter()
        binding.rvFullStackWebTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        val search = menu?.findItem(R.id.menu_search_email)
        val searchView = search?.actionView as SearchView

        searchView.queryHint = "Search in mail"

        return super.onCreateOptionsMenu(menu, inflater)
    }

}