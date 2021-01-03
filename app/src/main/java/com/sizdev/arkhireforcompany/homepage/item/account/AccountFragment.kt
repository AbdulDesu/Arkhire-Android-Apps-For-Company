package com.sizdev.arkhireforcompany.homepage.item.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentAccountBinding
import com.sizdev.arkhireforcompany.homepage.item.account.profile.CompanyProfileActivity
import com.sizdev.arkhireforcompany.networking.ApiClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_logout_confirmation.view.*
import kotlinx.coroutines.*


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var dialog: AlertDialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: AccountApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ApiClient.getApiClient(it) }!!.create(AccountApiService::class.java)

        // Get Saved Name
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val savedName = sharedPrefData.getString("accName", null)

        // Show Account Data
        if (savedName != null){
            showAccountData(savedName)
        }

        binding.tvLogout.setOnClickListener {
            startAlertLogoutConfirmation()
            dialog.show()
        }

        binding.tvMyProfile.setOnClickListener {
            val intent = Intent(activity, CompanyProfileActivity::class.java)
            startActivity(intent)
        }
        return  binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun showAccountData(accountHolder: String) {
        coroutineScope.launch {
            Log.d("Arkhire company", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("Arkhire company", "CallApi: ${Thread.currentThread().name}")
                try {
                    service?.getAccountDataByNameResponse(accountHolder)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is AccountResponse) {
                binding.tvFullNameAccount.text = result.data[0].accountName
                binding.tvCompanyName.text = "${result.data[0].companyName} (${result.data[0].companyPosition})"

                //Set Profile Images
                Picasso.get()
                        .load("http://54.82.81.23:911/image/${result.data[0].companyImage}")
                        .resize(512, 512)
                        .centerCrop()
                        .into(binding.ivCompanyProfileImage)
            }
        }
    }

    private fun startAlertLogoutConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_logout_confirmation, null)

        dialog = activity?.let {
            AlertDialog.Builder(it)
                .setView(view)
                .setCancelable(false)
                .create()
        }!!

        view.bt_yesLogout.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, LoginActivity::class.java)
            val sharedPref = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("accID", null)
            editor.apply()
            startActivity(intent)
            activity?.finish()
        }

        view.bt_noLogout.setOnClickListener {
            dialog.cancel()
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}