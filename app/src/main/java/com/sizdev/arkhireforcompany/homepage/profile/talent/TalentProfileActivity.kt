package com.sizdev.arkhireforcompany.homepage.profile.talent

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityTalentProfileBinding
import com.sizdev.arkhireforcompany.homepage.webviewer.ArkhireWebViewerActivity
import com.sizdev.arkhirefortalent.homepage.profile.TalentProfileTabAdapter
import kotlinx.android.synthetic.main.alert_call_confirmation.view.*
import kotlinx.android.synthetic.main.alert_hiring_confirmation.view.*
import kotlinx.android.synthetic.main.alert_mailing_confirmation.view.*

class TalentProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTalentProfileBinding
    private lateinit var pagerAdapter: TalentProfileTabAdapter
    private lateinit var dialog: AlertDialog
    private var doubleBackToExitPressedOnce = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_talent_profile)


        val lorem: String = getString(R.string.lorem_ipsum)
        pagerAdapter = TalentProfileTabAdapter(supportFragmentManager)
        binding.vpTalentProfile.adapter = pagerAdapter
        binding.tabTalentProfile.setupWithViewPager(binding.vpTalentProfile)
        binding.tvProfileTalentName.text = "Kiryuu Sento"
        binding.tvProfileTalentDesc.text = lorem

        binding.ivTalentPhone.setOnClickListener{
            startAlertCallConfirmation()
            dialog.show()
        }

        binding.ivTalentEmail.setOnClickListener {
            startAlertEmailConfirmation()
            dialog.show()
        }

        binding.ivTalentGithub.setOnClickListener {
            val intent = Intent(this, ArkhireWebViewerActivity::class.java)
            startActivity(intent)
        }

        binding.btDoHire.setOnClickListener {
            startAlertHireConfirmation()
            dialog.show()
        }
    }

    override fun onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tap back again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    @SuppressLint("InflateParams")
    private fun startAlertHireConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_hiring_confirmation, null)

        dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.bt_yesHire.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, CreateProjectActivity::class.java)
            startActivity(intent)
        }

        view.bt_noHire.setOnClickListener {
            dialog.cancel()
        }
    }

    @SuppressLint("InflateParams")
    private fun startAlertCallConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_call_confirmation, null)

        dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.bt_yesCall.setOnClickListener {
            dialog.dismiss()
            val call = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "+628152121123", null))
            try {
                startActivity(call)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Please Enable Call permission ", Toast.LENGTH_SHORT).show()
            }
        }

        view.bt_noCall.setOnClickListener {
            dialog.cancel()
        }
    }

    @SuppressLint("InflateParams")
    private fun startAlertEmailConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_mailing_confirmation, null)

        dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.bt_yesSend.setOnClickListener {
            dialog.dismiss()
            val sendEmail = Intent(Intent.ACTION_SENDTO)
            sendEmail.putExtra(Intent.EXTRA_EMAIL, "KiryuuSento@gmail.com")
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Arkhire Email")
            sendEmail.data = Uri.parse("mailto: KiryuuSento@gmail.com")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_FROM_BACKGROUND)
            try {
                startActivity(sendEmail)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Log.d("Email error:", e.toString())
            }
        }

        view.bt_noSend.setOnClickListener {
            dialog.cancel()
        }
    }
}