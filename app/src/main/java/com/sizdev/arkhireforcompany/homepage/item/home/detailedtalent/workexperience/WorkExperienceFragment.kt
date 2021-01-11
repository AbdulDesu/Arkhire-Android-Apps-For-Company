package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.workexperience

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentWorkExperienceBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import java.lang.Runnable


class WorkExperienceFragment : Fragment() {

    private lateinit var binding: FragmentWorkExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService
    private lateinit var mainHandler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_experience, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ArkhireApiClient.getApiClient(it) }!!.create(ArkhireApiService::class.java)

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Get Experience Owner
        val accountID = requireActivity().intent.getStringExtra("accountID")

        // Set Up RecyclerView
        binding.rvTalentWorkExperience.adapter = WorkExperienceAdapter()
        binding.rvTalentWorkExperience.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        // Data Refresh Management
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (accountID != null) {
                    showExperiences(accountID)
                }
                mainHandler.postDelayed(this, 5000)
            }
        })


        return binding.root
    }

    private fun showExperiences(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getWorkExperiences(accountID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is WorkExperienceResponse) {
                val list = result.data?.map{
                    WorkExperienceModel(it.experienceID, it.experienceOwner, it.experienceTitle, it.experienceSource, it.experienceStart, it.experienceEnd, it.experienceDesc)}

                (binding.rvTalentWorkExperience.adapter as WorkExperienceAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }

            else {
                // End Of Loading
                binding.loadingScreen.visibility = View.GONE

                // Show Empty Data Alert
                binding.rvTalentWorkExperience.visibility = View.GONE
                binding.emptyData.visibility = View.VISIBLE

            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}