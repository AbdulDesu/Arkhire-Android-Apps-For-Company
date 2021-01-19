package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityProjectHiringDetailBinding
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class ProjectHiringDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectHiringDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_hiring_detail)

        // Set Status Bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        val talentName = intent.getStringExtra("talentName")
        val talentTitle = intent.getStringExtra("talentTitle")
        val talentImage = intent.getStringExtra("talentImage")
        val projectTitle = intent.getStringExtra("projectTitle")
        val offeredSalary = format.format(intent.getStringExtra("projectSalary")?.toDouble())
        val projectDeadline = intent.getStringExtra("projectDeadline")
        val hiringStatus = intent.getStringExtra("hiringStatus")
        val projectImage = intent.getStringExtra("projectImage")
        val msgReply = intent.getStringExtra("replyMsg")

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "A talent $hiringStatus my project call : $projectTitle, Share You Tought with us at Arkhire !")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent, "Share to:"))
        }

        // Set Data
        binding.tvProjectTitle.text = projectTitle
        binding.tvDetailProjectSalary.text = offeredSalary
        binding.tvDuration.text = projectDeadline
        when (hiringStatus) {
            "Approved" -> binding.ivHiringStatus.setImageResource(R.drawable.ic_approved_detail)
            "Declined" -> binding.ivHiringStatus.setImageResource(R.drawable.ic_declined_detail)
            else -> binding.ivHiringStatus.setImageResource(R.drawable.ic_waiting_detail)
        }

        // Set Project Image
        Picasso.get()
            .load("http://54.82.81.23:911/image/$projectImage")
            .resize(1280, 720)
            .centerCrop()
            .into(binding.ivProjectCompanyPhoto)
        binding.tvReplyMsg.text = msgReply

        // Hired Image
        Picasso.get()
            .load("http://54.82.81.23:911/image/$talentImage")
            .resize(512, 512)
            .centerCrop()
            .into(binding.ivTalentImage)

        binding.tvTalentName.text = talentName
        binding.tvTalentTittle.text = talentTitle


    }
}