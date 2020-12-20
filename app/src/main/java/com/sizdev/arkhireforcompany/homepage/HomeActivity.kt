package com.sizdev.arkhireforcompany.homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.ActivityHomeBinding
import com.sizdev.arkhireforcompany.homepage.item.AccountFragment
import com.sizdev.arkhireforcompany.homepage.item.ChatFragment
import com.sizdev.arkhireforcompany.homepage.item.HomeFragment
import com.sizdev.arkhireforcompany.homepage.item.ProjectFragment


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    lateinit var homeFragment: HomeFragment
    lateinit var projectFragment: ProjectFragment
    lateinit var chatFragment: ChatFragment
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

                R.id.chat -> {
                    chatFragment = ChatFragment()
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
}