package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityProjectDetailBinding
import com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.TalentProfileTabAdapter
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*

class ProjectDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService
    private lateinit var pagerAdapter: ProjectDetailTabAdapter

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_detail)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ArkhireApiClient.getApiClient(this)!!.create(ArkhireApiService::class.java)

        //Profile Tab
        pagerAdapter = ProjectDetailTabAdapter(supportFragmentManager)
        binding.vpProjectDetails.adapter = pagerAdapter
        binding.tabProjectDetails.setupWithViewPager(binding.vpProjectDetails)

        //Set Action Bar
        setSupportActionBar(binding.tbDetailsProject)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tbDetailsProject.setNavigationOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}