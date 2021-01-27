package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.portfolio

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
import com.sizdev.arkhireforcompany.databinding.FragmentPortofolioBinding
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.*
import java.lang.Runnable


class PortofolioFragment : Fragment() {

    lateinit var binding: FragmentPortofolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService
    private lateinit var mainHandler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_portofolio, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ArkhireApiClient.getApiClient(it) }!!.create(ArkhireApiService::class.java)

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Get Portfolio Owner
        val accountID = requireActivity().intent.getStringExtra("accountID")

        // Data Refresh Management
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (accountID != null) {
                    showPortfolio(accountID)
                }
                mainHandler.postDelayed(this, 5000)
            }
        })

        // Set Recycler View
        binding.rvPortfolio.adapter = PortfolioAdapter()
        binding.rvPortfolio.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        return binding.root
    }

    private fun showPortfolio(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getPortfolio(accountID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is PortfolioResponse) {
                val list = result.data?.map{
                    PortfolioModel(it.portfolioID, it.portfolioOwner, it.portfolioTitle, it.portfolioDesc, it.portfolioRepository, it.portfolioImage)
                }

                (binding.rvPortfolio.adapter as PortfolioAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
                binding.emptyData.visibility = View.GONE
                binding.rvPortfolio.visibility = View.VISIBLE
            }

            else {
                // End Of Loading
                binding.loadingScreen.visibility = View.GONE

                // Show Empty Data Alert
                binding.rvPortfolio.visibility = View.GONE
                binding.emptyData.visibility = View.VISIBLE

            }
        }
    }

    override fun onDestroy() {
        mainHandler.removeCallbacksAndMessages(null)
        coroutineScope.cancel()
        super.onDestroy()
    }

}