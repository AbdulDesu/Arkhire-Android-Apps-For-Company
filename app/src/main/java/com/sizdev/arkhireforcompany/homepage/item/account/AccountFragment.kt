package com.sizdev.arkhireforcompany.homepage.item.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentAccountBinding
import com.sizdev.arkhireforcompany.homepage.profile.company.CompanyProfileActivity


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        // Get Saved Name
        val sharedPrefData = requireActivity().getSharedPreferences(
            "fullData",
            Context.MODE_PRIVATE
        )

        val savedName = sharedPrefData.getString("fullName", null)
        val savedCompany = sharedPrefData.getString("companyName", null)

        binding.tvFullNameAccount.text = savedName
        binding.tvCompanyName.text = savedCompany

        binding.tvLogout.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            logedOutSuccesfully()
            activity?.finish()
        }

        binding.tvMyProfile.setOnClickListener {
            val intent = Intent(activity, CompanyProfileActivity::class.java)
            startActivity(intent)
        }
        return  binding.root
    }

    private fun logedOutSuccesfully(){
        val sharedPref = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Login", false)
        editor.apply()
    }

}