package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.portfolio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityPortfolioDetailsBinding
import com.sizdev.arkhireforcompany.webviewer.ArkhireWebViewerActivity
import com.squareup.picasso.Picasso

class PortfolioDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPortfolioDetailsBinding

    private var portfolioTitle: String? = null
    private var portfolioDesc: String? = null
    private var portfolioRepo: String? = null
    private var portfolioImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_portfolio_details)

        // Get Data
        getData()

        // Set Data
        setData()

        // Repository Manager
        openRepo()

        // Back Button
        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun openRepo() {
        if(portfolioRepo != null){
            binding.btOpenRepository.setOnClickListener {
                val intent = Intent(this, ArkhireWebViewerActivity::class.java)
                intent.putExtra("url", portfolioRepo)
                startActivity(intent)
            }
        }
        else {
            binding.btOpenRepository.visibility = View.INVISIBLE
        }
    }

    private fun setData() {
        binding.tvPortfolioTitle.text = portfolioTitle
        binding.tvPortfolioDesc.text = portfolioDesc
        Picasso.get()
            .load("http://54.82.81.23:911/image/$portfolioImage")
            .resize(1024, 800)
            .centerCrop()
            .into(binding.ivPortfolioImage)

        //
    }

    private fun getData() {
        portfolioTitle = intent.getStringExtra("portfolioTitle")
        portfolioDesc = intent.getStringExtra("portfolioDesc")
        portfolioRepo = intent.getStringExtra("portfolioRepo")
        portfolioImage = intent.getStringExtra("portfolioImage")
    }
}