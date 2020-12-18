package com.sizdev.arkhireforcompany.onboarding.item

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sizdev.arkhireforcompany.R
import kotlinx.android.synthetic.main.fragment_second_screen.view.*


class SecondScreenFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second_screen, container, false)

        view.tv_start.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeViewPager_to_loginActivity)
            onBoardingFinish()
        }
        return view
    }

    private fun onBoardingFinish(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Complete", true)
        editor.apply()
    }

}