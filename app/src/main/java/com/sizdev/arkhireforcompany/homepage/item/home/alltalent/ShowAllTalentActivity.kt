package com.sizdev.arkhireforcompany.homepage.item.home.alltalent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityShowAllTalentBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*

class ShowAllTalentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowAllTalentBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_all_talent)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ArkhireApiClient.getApiClient(this)!!.create(ArkhireApiService::class.java)

        setSupportActionBar(binding.tbShowAllProject)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tbShowAllProject.setNavigationOnClickListener {
            finish()
        }

        showAllTalent()
        binding.rvShowAllTalent.adapter = ShowAllTalentAdapter()
        binding.rvShowAllTalent.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun showAllTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllTalentResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ShowAllTalentResponse) {
                val list = result.data?.map {
                    ShowAllTalentModel(
                        it.talentID,
                        it.accountID,
                        it.accountName,
                        it.accountEmail,
                        it.accountPhone,
                        it.talentTitle,
                        it.talentTime,
                        it.talentCity,
                        it.talentDesc,
                        it.talentImage,
                        it.talentGithub,
                        it.talentCv,
                        it.talentSkill1,
                        it.talentSkill2,
                        it.talentSkill3,
                        it.talentSkill4,
                        it.talentSkill5
                    )
                }

                (binding.rvShowAllTalent.adapter as ShowAllTalentAdapter).addList(list)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}