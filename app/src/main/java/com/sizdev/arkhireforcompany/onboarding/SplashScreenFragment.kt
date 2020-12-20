package com.sizdev.arkhireforcompany.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentSplashScreenBinding
import com.sizdev.arkhireforcompany.homepage.HomeActivity

class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false)
        Handler().postDelayed({
            if(onBoardingFinish()){
                if (logedInSuccesfully()){
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                else{
                    findNavController().navigate(R.id.action_splashScreenFragment_to_loginActivity)
                }
            }
            else{
                findNavController().navigate(R.id.action_splashScreenFragment_to_welcomeViewPager)
            }
        }, 3000)

        return binding.root
    }

    private fun onBoardingFinish(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Complete", false)
    }

    private fun logedInSuccesfully(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Login", false)
    }



}