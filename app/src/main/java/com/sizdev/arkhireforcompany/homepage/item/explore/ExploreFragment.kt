package com.sizdev.arkhireforcompany.homepage.item.explore

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentExploreBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import java.lang.Runnable


class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ArkhireApiClient.getApiClient(it) }!!.create(ArkhireApiService::class.java)

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Data Refresh Management
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                showTalent()
                mainHandler.postDelayed(this, 60000)
            }
        })

        // Set Up RecyclerView
        binding.rvExplore.adapter = ExploreAdapter()
        binding.rvExplore.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)

        // Manage Pop Up
        val filterBy = PopupMenu(activity, binding.filter)
        filterBy.menu.add(Menu.NONE, 0 ,0, "Location")
        filterBy.menu.add(Menu.NONE, 1 ,1, "WorkTime")



        // Set Search View
        binding.tbExplore.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.tbExplore.clearFocus()
                if (query != null) {
                    binding.rvExplore.visibility = View.VISIBLE
                    binding.lnNotFound.visibility = View.GONE
                    mainHandler.removeCallbacksAndMessages(null)
                    filterByName(query)

                    binding.filter.setOnClickListener {

                        // Pop Up Listener
                        binding.loadingScreen.visibility = View.VISIBLE
                        filterBy.setOnMenuItemClickListener { menuItem ->
                            val id = menuItem.itemId

                            when (id) {
                                0 -> filterByLocation(query)
                                1 -> filterByTime(query)
                            }
                            false
                        }

                        filterBy.show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    mainHandler.removeCallbacksAndMessages(null)
                    filterByName(newText)
                }
                return false
            }
        })

        binding.tbExplore.setOnCloseListener {
            binding.tbExplore.clearFocus()
            showTalent()
            false
        }

        return binding.root
    }

    private fun showTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllTalentResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ExploreResponse) {
                val list = result.data?.map {
                    ExploreModel(
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

                (binding.rvExplore.adapter as ExploreAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }
        }
    }

    private fun filterByName(talentName: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.filterTalentByName(talentName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ExploreResponse) {
                val list = result.data?.map {
                    ExploreModel(
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

                (binding.rvExplore.adapter as ExploreAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }
        }
    }

    private fun filterByLocation(talentLocation: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.filterTalentByLocation(talentLocation)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ExploreResponse) {
                val list = result.data?.map {
                    ExploreModel(
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

                (binding.rvExplore.adapter as ExploreAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }
        }
    }

    private fun filterByTime(talentTime: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.filterTalentByTimeWork(talentTime)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ExploreResponse) {
                val list = result.data?.map {
                    ExploreModel(
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

                (binding.rvExplore.adapter as ExploreAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }
        }
    }



    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}