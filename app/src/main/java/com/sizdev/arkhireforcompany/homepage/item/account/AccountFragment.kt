package com.sizdev.arkhireforcompany.homepage.item.account

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentAccountBinding
import com.sizdev.arkhireforcompany.homepage.item.account.profile.CompanyProfileActivity
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileActivity
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_logout_confirmation.view.*
import kotlinx.coroutines.*


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var dialog: AlertDialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ArkhireApiService

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = activity?.let { ArkhireApiClient.getApiClient(it) }!!.create(ArkhireApiService::class.java)

        // Data Loading Management
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100

        // Get Saved ID
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val savedID = sharedPrefData.getString("accID", null)

        // Set Rate Us
        binding.tvRateUs.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=Arkhire")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=Arkhire")
                    )
                )
            }
        }

        // Show Account Data
        if (savedID != null){
            showAccountData(savedID)
        }

        binding.tvLogout.setOnClickListener {
            startAlertLogoutConfirmation()
            dialog.show()
        }

        return  binding.root
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    private fun showAccountData(accountHolder: String) {
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

                binding.tvFullNameAccount.text = result.data[0].accountName
                binding.tvCompanyName.text = "${result.data[0].companyName} (${result.data[0].companyPosition})"
                binding.tvMyProfile.setOnClickListener {
                    val intent = Intent(activity, CompanyProfileActivity::class.java)
                    intent.putExtra("companyID", result.data[0].companyID)
                    intent.putExtra("companyName", result.data[0].companyName)
                    intent.putExtra("companyType", result.data[0].companyType)
                    intent.putExtra("companyImage", result.data[0].companyImage)
                    intent.putExtra("companyLinkedin", result.data[0].companyLinkedin)
                    intent.putExtra("companyInstagram", result.data[0].companyInstagram)
                    intent.putExtra("companyFacebook", result.data[0].companyFacebook)
                    intent.putExtra("companyDesc", result.data[0].companyDesc)
                    intent.putExtra("companyLatitude", result.data[0].companyLatitude)
                    intent.putExtra("companyLongitude", result.data[0].companyLongitude)

                    if(result.data[0].companyType == null){
                        val intent = Intent(activity, CompanyEditProfileActivity::class.java)
                        intent.putExtra("companyID", result.data[0].companyID)
                        intent.putExtra("companyName", result.data[0].companyName)
                        intent.putExtra("companyType", result.data[0].companyType)
                        startActivity(intent)
                    }
                    else {
                        startActivity(intent)
                    }

                }

                //Set Profile Images
                when(result.data[0].companyImage){
                    null -> binding.ivCompanyProfileImage.setImageResource(R.drawable.ic_empty_image)
                    else -> {
                        Picasso.get()
                                .load("http://54.82.81.23:911/image/${result.data[0].companyImage}")
                                .resize(512, 512)
                                .centerCrop()
                                .into(binding.ivCompanyProfileImage)
                    }
                }

                // End Of Loading
                Handler().postDelayed({
                    binding.loadingScreen.visibility = View.GONE
                }, 2000)
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