package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import com.sizdev.arkhireforcompany.homepage.item.project.createhiring.CreateHiringActivity
import com.sizdev.arkhireforcompany.homepage.item.project.createproject.CreateProjectActivity
import com.sizdev.arkhireforcompany.webviewer.ArkhireWebViewerActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_call_confirmation.view.*
import kotlinx.android.synthetic.main.alert_hiring_confirmation.view.*
import kotlinx.android.synthetic.main.alert_mailing_confirmation.view.*

class TalentProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTalentProfileBinding
    private lateinit var pagerAdapter: TalentProfileTabAdapter
    private lateinit var dialog: AlertDialog
    private var phoneNumber : String? = null

    companion object {
        private const val PERMISSION_CODE = 911
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_talent_profile)

        //Profile Tab
        pagerAdapter = TalentProfileTabAdapter(supportFragmentManager)
        binding.vpTalentProfile.adapter = pagerAdapter
        binding.tabTalentProfile.setupWithViewPager(binding.vpTalentProfile)

        //Get Data
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

        if(talentTime == "Freelance"){
            binding.ivTalentProfileCover.setImageResource(R.drawable.ic_freelancer)
        }
        else {
            binding.ivTalentProfileCover.setImageResource(R.drawable.ic_fulltimework)
        }

        Picasso.get()
                .load("http://54.82.81.23:911/image/$talentImage")
                .resize(512, 512)
                .centerCrop()
                .into(binding.ivTalentImageProfile)

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
            if (talentGithub != "null"){
                intent.putExtra("url", "https://github.com/$talentGithub")
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "This talent not have github account", Toast.LENGTH_SHORT).show()
            }
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

    @SuppressLint("InflateParams")
    private fun startAlertHireConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_hiring_confirmation, null)

        dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.bt_yesHire.setOnClickListener {
            dialog.dismiss()

            // Get Data Saved
            val talentID = intent.getStringExtra("talentID")
            val talentName = intent.getStringExtra("talentName")
            val talentTitle = intent.getStringExtra("talentTitle")
            val talentImage = intent.getStringExtra("talentImage")

            val intent = Intent(this, CreateHiringActivity::class.java)
            intent.putExtra("talentID", talentID)
            intent.putExtra("talentName", talentName)
            intent.putExtra("talentTitle", talentTitle)
            intent.putExtra("talentImage", talentImage)

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
        phoneNumber = talentPhone
        dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.tv_alertNumberTalent.text = talentPhone
        view.bt_yesCall.setOnClickListener {
            dialog.dismiss()
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.CALL_PHONE);
                //show popup to request runtime permission
                requestPermissions(permissions, TalentProfileActivity.PERMISSION_CODE);
            }
            else{
                //permission already granted
                callTalent(talentPhone!!)
            }
        }

        view.bt_noCall.setOnClickListener {
            dialog.cancel()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when(requestCode) {
            TalentProfileActivity.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    callTalent(phoneNumber!!)
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show()
                }
            }
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

    private fun callTalent(talentPhone: String) {
        val call = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", talentPhone, null))
        try {
            startActivity(call )
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}