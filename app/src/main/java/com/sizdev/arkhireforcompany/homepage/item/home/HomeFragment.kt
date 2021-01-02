package com.sizdev.arkhireforcompany.homepage.item.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentHomeBinding
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperApiService
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperModel
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperResponse
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerApiService
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerModel
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileApiService
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebApiService
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebResponse
import com.sizdev.arkhireforcompany.networking.ApiClient
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var serviceAndroidDeveloper: AndroidDeveloperApiService
    private lateinit var serviceDevOpsEngineer: DevOpsEngineerApiService
    private lateinit var serviceFullStackMobile: FullStackMobileApiService
    private lateinit var serviceFullStackWeb: FullStackWebApiService

    @SuppressLint("SimpleDateFormat", "WeekBasedYear", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        serviceAndroidDeveloper = ApiClient.getApiClient(requireActivity())!!.create(AndroidDeveloperApiService::class.java)
        serviceDevOpsEngineer = ApiClient.getApiClient(requireActivity())!!.create(DevOpsEngineerApiService::class.java)
        serviceFullStackMobile = ApiClient.getApiClient(requireActivity())!!.create(FullStackMobileApiService::class.java)
        serviceFullStackWeb = ApiClient.getApiClient(requireActivity())!!.create(FullStackWebApiService::class.java)

        // Get Date
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM YYYY")
        val currentDate = dateFormat.format(Date())

        // Get Saved name
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val savedData = sharedPrefData.getString("accName", null)
        val nameSplitter = savedData?.split(" ")

        // Get Hour
        val c = Calendar.getInstance()
        val timeOfDay = c[Calendar.HOUR_OF_DAY]

        binding.tvHomeDate.text = currentDate
        if (nameSplitter?.size == 1){
            if (timeOfDay in 0..11) {
                binding.tvUserGreeting.text = "Good Morning, ${savedData.capitalize()}"
            } else if (timeOfDay in 12..15) {
                binding.tvUserGreeting.text = "Good Afternoon, ${savedData.capitalize()}"
            } else if (timeOfDay in 16..20) {
                binding.tvUserGreeting.text = "Good Evening, ${savedData.capitalize()}"
            } else if (timeOfDay in 21..23) {
                binding.tvUserGreeting.text = "Good Night, ${savedData.capitalize()}"
            }
        }
        else {
            val lastName = nameSplitter?.get(1).toString()
            if (timeOfDay in 0..11) {
                binding.tvUserGreeting.text = "Good Morning, $lastName"
            } else if (timeOfDay in 12..15) {
                binding.tvUserGreeting.text = "Good Afternoon, $lastName"
            } else if (timeOfDay in 16..20) {
                binding.tvUserGreeting.text = "Good Evening, $lastName"
            } else if (timeOfDay in 21..23) {
                binding.tvUserGreeting.text = "Good Night, $lastName"
            }
        }

        showAndroidDeveloperTalent()
        binding.rvAndroidDeveloperTalent.adapter = AndroidDeveloperAdapter()
        binding.rvAndroidDeveloperTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        showDevOpsEngineerTalent()
        binding.rvDevOpsEngineerTalent.adapter = DevOpsEngineerAdapter()
        binding.rvDevOpsEngineerTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        showFullStackMobileTalent()
        binding.rvFullStackMobileTalent.adapter = FullStackMobileAdapter()
        binding.rvFullStackMobileTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        showFullStackWebTalent()
        binding.rvFullStackWebTalent.adapter = FullStackWebAdapter()
        binding.rvFullStackWebTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        return binding.root
    }


    private fun showAndroidDeveloperTalent() {
        coroutineScope.launch {
            Log.d("Arkhire Talent", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Talent", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceAndroidDeveloper?.getTalentAndroidResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is AndroidDeveloperResponse) {
                Log.d("Arkhire Talent", result.toString())
                val list = result.data?.map{
                    AndroidDeveloperModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }

                (binding.rvAndroidDeveloperTalent.adapter as AndroidDeveloperAdapter).addList(list)
            }

        }
    }

    private fun showDevOpsEngineerTalent() {
        coroutineScope.launch {
            Log.d("Arkhire Talent", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Talent", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceDevOpsEngineer?.getTalentDevOpsResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is DevOpsEngineerResponse) {
                Log.d("Arkhire Talent", result.toString())
                val list = result.data?.map{
                    DevOpsEngineerModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }

                (binding.rvDevOpsEngineerTalent.adapter as DevOpsEngineerAdapter).addList(list)
            }
        }
    }

    private fun showFullStackMobileTalent() {
        coroutineScope.launch {
            Log.d("Arkhire Talent", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Talent", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceFullStackMobile?.getTalentFullStackMobileResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is FullStackMobileResponse) {
                Log.d("Arkhire Talent", result.toString())
                val list = result.data?.map{
                    FullStackMobileModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }

                (binding.rvFullStackMobileTalent.adapter as FullStackMobileAdapter).addList(list)
            }
        }
    }

    private fun showFullStackWebTalent() {
        coroutineScope.launch {
            Log.d("Arkhire Talent", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Talent", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceFullStackWeb?.getTalentFullStackWebResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is FullStackWebResponse) {
                Log.d("Arkhire Talent", result.toString())
                val list = result.data?.map{
                    FullStackWebModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }

                (binding.rvFullStackWebTalent.adapter as FullStackWebAdapter).addList(list)
            }
        }
    }





    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}