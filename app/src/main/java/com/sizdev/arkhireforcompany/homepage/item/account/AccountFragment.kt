package com.sizdev.arkhireforcompany.homepage.item.account

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel


class AccountFragment : Fragment(), AccountContract.View {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var dialog: AlertDialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var handler: Handler

    private var accountID: String? = null
    private var presenter: AccountPresenter? = null

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        // Set Services
        setService()

        // Show Progress Bar
        showProgressBar()

        // Get Current Login data
        getCurrentLoginData()

        // Set Data Refresh Management
        setDataRefreshManagement()

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

        binding.tvLogout.setOnClickListener {
            startAlertLogoutConfirmation()
            dialog.show()
        }

        return  binding.root
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

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                handler.removeCallbacksAndMessages(null)
                showSessionExpiredAlert()
                dialog.show()
            }
            else -> {
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = AccountPresenter(coroutineScope, service)
    }

    override fun getCurrentLoginData() {
        val sharedPrefData = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        accountID = sharedPrefData.getString("accID", null)
    }

    @SuppressLint("SetTextI18n")
    override fun setAccountData(companyID: String, accountID: String, accountName: String, companyName: String, companyType: String, companyPosition: String, companyImage: String, companyLatitude: String, companyLongitude: String) {
        binding.tvFullNameAccount.text = accountName
        binding.tvCompanyName.text = "$companyName($companyPosition)"

        binding.tvMyProfile.setOnClickListener {
            val intent = Intent(activity, CompanyProfileActivity::class.java)
            intent.putExtra("companyID", companyID)

            if(companyType == "null"){
                val intent2 = Intent(activity, CompanyEditProfileActivity::class.java)
                intent2.putExtra("companyID", companyID)
                startActivity(intent2)
            }
            else {
                intent.putExtra("companyLatitude", companyLatitude)
                intent.putExtra("companyLongitude", companyLatitude)
                startActivity(intent)
            }
        }

        //Set Profile Images
        when(companyImage){
            "null" -> binding.ivCompanyProfileImage.setImageResource(R.drawable.ic_empty_image)
            else -> {
                Picasso.get()
                        .load("http://54.82.81.23:911/image/$companyImage")
                        .resize(512, 512)
                        .centerCrop()
                        .into(binding.ivCompanyProfileImage)
            }
        }

    }

    override fun setDataRefreshManagement() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getAccountData(accountID!!)
                handler.postDelayed(this, 5000)
            }
        })
    }

    override fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    override fun showSessionExpiredAlert() {
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
}