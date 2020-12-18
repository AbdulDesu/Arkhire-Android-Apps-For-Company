package com.sizdev.arkhireforcompany.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.onboarding.item.FirstScreenFragment
import com.sizdev.arkhireforcompany.onboarding.item.SecondScreenFragment
import kotlinx.android.synthetic.main.fragment_welcome_view_pager.view.*


class WelcomeViewPager : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreenFragment(),
            SecondScreenFragment(),
        )

        val adapter = AdapterWelcome(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.welcomeViewPager.adapter = adapter

        return view
    }


}