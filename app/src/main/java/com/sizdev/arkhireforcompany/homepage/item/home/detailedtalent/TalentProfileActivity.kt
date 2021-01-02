package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityTalentProfileBinding
import com.sizdev.arkhireforcompany.homepage.webviewer.ArkhireWebViewerActivity
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

        //Profile Tab
        pagerAdapter = TalentProfileTabAdapter(supportFragmentManager)
        binding.vpTalentProfile.adapter = pagerAdapter
        binding.tabTalentProfile.setupWithViewPager(binding.vpTalentProfile)

        //Get Data
        val talentID = intent.getStringExtra("talentID")
        val accountID = intent.getStringExtra("accountID")
        val talentName = intent.getStringExtra("talentName")
        val talentTitle = intent.getStringExtra("talentTitle")
        val talentTime = intent.getStringExtra("talentTime")
        val talentLocation = intent.getStringExtra("talentLocation")
        val talentDesc = intent.getStringExtra("talentDesc")
        val talentImage = intent.getStringExtra("talentImage")
        val talentGithub = intent.getStringExtra("talentGithub")
        val talentSkill1 = intent.getStringExtra("talentSkill1")
        val talentSkill2 = intent.getStringExtra("talentSkill2")
        val talentSkill3 = intent.getStringExtra("talentSkill3")
        val talentSkill4 = intent.getStringExtra("talentSkill4")
        val talentSkill5 = intent.getStringExtra("talentSkill5")

        //Set Data
        binding.tvProfileTalentName.text = talentName
        binding.tvProfileTalentTitle.text = talentTitle
        binding.tvProfileTalentLocation.text = talentLocation
        binding.tvProfileTalentDesc.text = talentDesc
        binding.tvTitleProfileTalentSkill1.text = talentSkill1
        binding.tvTitleProfileTalentSkill2.text = talentSkill2
        binding.tvTitleProfileTalentSkill3.text = talentSkill3
        binding.tvTitleProfileTalentSkill4.text = talentSkill4
        binding.tvTitleProfileTalentSkill5.text = talentSkill5

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
            intent.putExtra("url", talentGithub)
            startActivity(intent)
        }

        binding.btDoHire.setOnClickListener {
            startAlertHireConfirmation()
            dialog.show()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.menuButton.setOnClickListener {
            val showMenu = PopupMenu(this, binding.menuButton)
            val talentCv = intent.getStringExtra("talentCv")
            showMenu.menu.add(Menu.NONE, 0 ,0, "Show CV")
            showMenu.menu.add(Menu.NONE, 1 ,1, "Report Talent")
            showMenu.show()

            showMenu.setOnMenuItemClickListener { menuItem ->
                val id = menuItem.itemId
                when (id) {
                    0 -> {
                        val intent = Intent(this, ArkhireWebViewerActivity::class.java)
                        intent.putExtra("webScale", "100")
                        intent.putExtra("url", "http://54.82.81.23:911/image/$talentCv")
                        startActivity(intent)
                    }
                    1 -> Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                }
                false
            }
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
        val talentPhone = intent.getStringExtra("talentPhone")
        dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.tv_alertNumberTalent.text = talentPhone
        view.bt_yesCall.setOnClickListener {
            dialog.dismiss()
            val call = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", talentPhone, null))
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
        val talentEmail = intent.getStringExtra("talentEmail")
        dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.tv_alertEmailAddressTalent.text = talentEmail

        view.bt_yesSend.setOnClickListener {
            dialog.dismiss()
            val sendEmail = Intent(Intent.ACTION_SENDTO)
            sendEmail.putExtra(Intent.EXTRA_EMAIL, talentEmail)
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Arkhire Email")
            sendEmail.data = Uri.parse("mailto: $talentEmail")
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