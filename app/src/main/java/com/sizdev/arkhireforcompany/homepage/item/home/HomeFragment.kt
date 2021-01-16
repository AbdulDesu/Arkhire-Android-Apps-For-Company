package com.sizdev.arkhireforcompany.homepage.item.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentHomeBinding
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperModel
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebModel
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), HomeContract.View {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialog: AlertDialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var handler: Handler

    private var accountID: String? = null
    private var presenter: HomePresenter? = null

    @SuppressLint("SimpleDateFormat", "WeekBasedYear", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // Set Service
        setService()

        // Show Progressbar
        showProgressBar()

        // Set Date
        setDate()

        // Get Current Login Data
        getCurrentAccount()

        // Set Up RecyclerView
        setRecyclerView()

        // Data Refresh Management
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getCurrentAccount(accountID!!)
                presenter?.getAndroidTalent()
                presenter?.getDevOpsTalent()
                presenter?.getFullStackMobileTalent()
                presenter?.getFullStackWebTalent()
                handler.postDelayed(this, 5000)
            }
        })


        return binding.root
    }

    private fun setRecyclerView() {
        binding.rvAndroidDeveloperTalent.adapter = AndroidDeveloperAdapter()
        binding.rvAndroidDeveloperTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)


        binding.rvDevOpsEngineerTalent.adapter = DevOpsEngineerAdapter()
        binding.rvDevOpsEngineerTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)


        binding.rvFullStackMobileTalent.adapter = FullStackMobileAdapter()
        binding.rvFullStackMobileTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        binding.rvFullStackWebTalent.adapter = FullStackWebAdapter()
        binding.rvFullStackWebTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    private fun setDate() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM YYYY")
        val currentDate = dateFormat.format(Date())

        binding.tvHomeDate.text = currentDate
    }

    private fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    private fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = HomePresenter(coroutineScope, service)
    }

    @SuppressLint("SetTextI18n")
    override fun setGreeting(name: String) {

        // Split The Name
        val nameSplitter = name.split(" ")

        // Get Hour
        val c = Calendar.getInstance()
        val timeOfDay = c[Calendar.HOUR_OF_DAY]

        if (nameSplitter.size == 1){
            when (timeOfDay) {
                in 0..11 -> binding.tvUserGreeting.text = "Good Morning, ${name.capitalize(Locale.ROOT)}"
                in 12..15 -> binding.tvUserGreeting.text = "Good Afternoon, ${name.capitalize(Locale.ROOT)}"
                in 16..20 -> binding.tvUserGreeting.text = "Good Evening, ${name.capitalize(Locale.ROOT)}"
                in 21..23 -> binding.tvUserGreeting.text = "Good Night, ${name.capitalize(Locale.ROOT)}"
            }
        }

        else {
            val lastName = nameSplitter[1]
            when (timeOfDay){
                in 0..11 -> binding.tvUserGreeting.text = "Good Morning, $lastName"
                in 12..15 -> binding.tvUserGreeting.text = "Good Afternoon, $lastName"
                in 16..20 -> binding.tvUserGreeting.text = "Good Evening, $lastName"
                in 21..23 -> binding.tvUserGreeting.text = "Good Night, $lastName"
            }
        }
    }

    override fun setError(error: String) {
        if (error == "Session Expired !"){
            sessionExpiredAlert()
            dialog.show()
        }

        else {
            binding.rvAndroidDeveloperTalent.visibility = View.VISIBLE
            binding.rvDevOpsEngineerTalent.visibility = View.VISIBLE
            binding.rvFullStackMobileTalent.visibility = View.VISIBLE
            binding.rvFullStackWebTalent.visibility = View.VISIBLE
        }

    }

    private fun sessionExpiredAlert() {
        val view: View = layoutInflater.inflate(R.layout.alert_session_expired, null)

        dialog = activity?.let {
            AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .create()
        }!!

        view.bt_okRelog.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, LoginActivity::class.java)
            val sharedPref = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("accID", null)
            editor.apply()
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun getCurrentAccount() {
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        accountID = sharedPrefData.getString("accID", null)
    }

    override fun setCompanyID(companyID: String) {
        val sharedPref = activity?.getSharedPreferences("Token", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString("accCompany", companyID)
        editor?.apply()
    }

    override fun addAndroidDeveloperTalent(list: List<AndroidDeveloperModel>) {
        (binding.rvAndroidDeveloperTalent.adapter as AndroidDeveloperAdapter).addList(list)
    }

    override fun addDevOpsEngineerTalent(list: List<DevOpsEngineerModel>) {
        (binding.rvDevOpsEngineerTalent.adapter as DevOpsEngineerAdapter).addList(list)
    }

    override fun addFullStackMobileTalent(list: List<FullStackMobileModel>) {
        (binding.rvFullStackMobileTalent.adapter as FullStackMobileAdapter).addList(list)
    }

    override fun addFullStackWebTalent(list: List<FullStackWebModel>) {
        (binding.rvFullStackWebTalent.adapter as FullStackWebAdapter).addList(list)
    }
}