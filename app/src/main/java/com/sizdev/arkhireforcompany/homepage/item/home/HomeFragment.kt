package com.sizdev.arkhireforcompany.homepage.item.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
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
import kotlinx.android.synthetic.main.alert_logout_confirmation.view.*
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialog: AlertDialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: HomeApiService
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
        service = ApiClient.getApiClient(requireActivity())!!.create(HomeApiService::class.java)
        serviceAndroidDeveloper = ApiClient.getApiClient(requireActivity())!!.create(AndroidDeveloperApiService::class.java)
        serviceDevOpsEngineer = ApiClient.getApiClient(requireActivity())!!.create(DevOpsEngineerApiService::class.java)
        serviceFullStackMobile = ApiClient.getApiClient(requireActivity())!!.create(FullStackMobileApiService::class.java)
        serviceFullStackWeb = ApiClient.getApiClient(requireActivity())!!.create(FullStackWebApiService::class.java)

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

    @SuppressLint("SetTextI18n")
    private fun showAccountOwner(accountID: String) {
        coroutineScope.launch {
            Log.d("Arkhire Company", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Company", "CallApi: ${Thread.currentThread().name}")
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
            Log.d("Arkhire Company", "Start: ${Thread.currentThread().name}")
            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Company", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceAndroidDeveloper?.getTalentAndroidResponse()
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
            Log.d("Arkhire Company", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Company", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceDevOpsEngineer?.getTalentDevOpsResponse()
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
            Log.d("Arkhire Company", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Company", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceFullStackMobile?.getTalentFullStackMobileResponse()
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
            Log.d("Arkhire Company", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire Company", "CallApi: ${Thread.currentThread().name}")
                try {
                    serviceFullStackWeb?.getTalentFullStackWebResponse()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is FullStackWebResponse) {
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