package com.sizdev.arkhireforcompany.onboarding.item

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentWelcomeViewPagerBinding


class SplashScreenViewPager : Fragment() {

    private lateinit var binding: FragmentWelcomeViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreenFragment(),
            SecondScreenFragment(),
        )

        val adapter = AdapterWelcome(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.welcomeViewPager.adapter = adapter
        binding.indicator.setViewPager2(binding.welcomeViewPager)

        return binding.root

    }


}