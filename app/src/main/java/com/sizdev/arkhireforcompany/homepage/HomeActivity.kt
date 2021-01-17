package com.sizdev.arkhireforcompany.homepage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityHomeBinding
import com.sizdev.arkhireforcompany.homepage.item.account.AccountFragment
import com.sizdev.arkhireforcompany.homepage.item.explore.ExploreFragment
import com.sizdev.arkhireforcompany.homepage.item.home.HomeFragment
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectFragment


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var doubleBackToExitPressedOnce = false

    lateinit var homeFragment: HomeFragment
    lateinit var projectFragment: ProjectFragment
    lateinit var chatFragment: ExploreFragment
    lateinit var accountFragment: AccountFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        //Launch Home Fragment At First
        binding.homeNavigationBar.setItemSelected(R.id.home)
        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.homeViewer, homeFragment)
            .commit()

        binding.homeNavigationBar.setOnItemSelectedListener { id ->
            when (id) {
                R.id.home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeViewer, homeFragment)
                        .commit()
                }

                R.id.makeCompanyProject -> {
                    projectFragment = ProjectFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeViewer, projectFragment)
                        .commit()
                }

                R.id.explore -> {
                    chatFragment = ExploreFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeViewer, chatFragment)
                        .commit()
                }

                R.id.account -> {
                    accountFragment = AccountFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeViewer, accountFragment)
                        .commit()
                }
            }
            true
        }
    }

    override fun onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tap back again to exit from apps", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}