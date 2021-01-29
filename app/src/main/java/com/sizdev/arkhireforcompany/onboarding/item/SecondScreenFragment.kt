package com.sizdev.arkhireforcompany.onboarding.item

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentSecondScreenBinding


class SecondScreenFragment : Fragment() {

    private lateinit var binding: FragmentSecondScreenBinding
    private lateinit var animation: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_screen, container, false)
        animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        binding.tvStart.startAnimation(animation)
        binding.tvStart.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeViewPager_to_loginActivity)
            onBoardingFinish()
        }

        return binding.root
    }

    private fun onBoardingFinish(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Complete", true)
        editor.apply()
    }

}