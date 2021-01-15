package com.sizdev.arkhireforcompany.homepage.item.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentHomeBinding
import com.sizdev.arkhireforcompany.homepage.item.account.AccountResponse
import com.sizdev.arkhireforcompany.homepage.item.account.profile.CompanyProfileActivity
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileActivity
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperModel
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperResponse
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerModel
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebAdapter
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialog: AlertDialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    @SuppressLint("SimpleDateFormat", "WeekBasedYear", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ArkhireApiClient.getApiClient(requireActivity())!!.create(ArkhireApiService::class.java)

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Get Date
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM YYYY")
        val currentDate = dateFormat.format(Date())

        // Get Saved name
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val accountID = sharedPrefData.getString("accID", null)

        // Show Current Loged In User
        if (accountID != null) {
            showAccountOwner(accountID)
        }

        binding.tvHomeDate.text = currentDate

        // Data Refresh Management
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                getCompanyData(accountID!!)
                showAndroidDeveloperTalent()
                showDevOpsEngineerTalent()
                showFullStackMobileTalent()
                showFullStackWebTalent()
                mainHandler.postDelayed(this, 5000)
            }
        })


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

    @SuppressLint("SetTextI18n")
    private fun showAccountOwner(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAccountResponse(accountID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is HomeResponse){
                val accountName = result.data.accountName
                // Split The Name
                val nameSplitter = accountName?.split(" ")

                // Get Hour
                val c = Calendar.getInstance()
                val timeOfDay = c[Calendar.HOUR_OF_DAY]

                if (nameSplitter?.size == 1){
                    when (timeOfDay) {
                        in 0..11 -> binding.tvUserGreeting.text = "Good Morning, ${accountName.capitalize()}"
                        in 12..15 -> binding.tvUserGreeting.text = "Good Afternoon, ${accountName.capitalize()}"
                        in 16..20 -> binding.tvUserGreeting.text = "Good Evening, ${accountName.capitalize()}"
                        in 21..23 -> binding.tvUserGreeting.text = "Good Night, ${accountName.capitalize()}"
                    }
                }
                else {
                    val lastName = nameSplitter?.get(1).toString()
                    when (timeOfDay){
                        in 0..11 -> binding.tvUserGreeting.text = "Good Morning, $lastName"
                        in 12..15 -> binding.tvUserGreeting.text = "Good Afternoon, $lastName"
                        in 16..20 -> binding.tvUserGreeting.text = "Good Evening, $lastName"
                        in 21..23 -> binding.tvUserGreeting.text = "Good Night, $lastName"
                    }
                }
                binding.layoutFragmentHome.visibility = View.VISIBLE
            }
            else {
                sessionExpiredAlert()
                dialog.show()
            }
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


    private fun showAndroidDeveloperTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentAndroidResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is AndroidDeveloperResponse) {
                val list = result.data?.map{
                    AndroidDeveloperModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }

                (binding.rvAndroidDeveloperTalent.adapter as AndroidDeveloperAdapter).addList(list)
            }
        }
    }

    private fun showDevOpsEngineerTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentDevOpsResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is DevOpsEngineerResponse) {
                val list = result.data?.map{
                    DevOpsEngineerModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }

                (binding.rvDevOpsEngineerTalent.adapter as DevOpsEngineerAdapter).addList(list)
            }
        }
    }

    private fun showFullStackMobileTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentFullStackMobileResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is FullStackMobileResponse) {
                val list = result.data?.map{
                    FullStackMobileModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }

                (binding.rvFullStackMobileTalent.adapter as FullStackMobileAdapter).addList(list)
            }
        }
    }

    private fun showFullStackWebTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentFullStackWebResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is FullStackWebResponse) {
                val list = result.data?.map{
                    FullStackWebModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                }
                (binding.rvFullStackWebTalent.adapter as FullStackWebAdapter).addList(list)

                // End Of Loading
                binding.loadingScreen.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    private fun getCompanyData(accountHolder: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAccountDataByNameResponse(accountHolder)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is AccountResponse) {

                // Save Emergency companyID
                val sharedPref = activity?.getSharedPreferences("Token", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putString("accCompany", result.data[0].companyID)
                editor?.apply()

            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}