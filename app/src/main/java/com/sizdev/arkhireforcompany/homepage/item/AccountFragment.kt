package com.sizdev.arkhireforcompany.homepage.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.LoginActivity
import com.sizdev.arkhireforcompany.homepage.profile.CompanyProfileActivity
import kotlinx.android.synthetic.main.activity_company_profile.*
import kotlinx.android.synthetic.main.fragment_account.view.*


class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_account, container, false)

        // Get Saved Name
        val sharedPrefData = requireActivity().getSharedPreferences(
            "fullData",
            Context.MODE_PRIVATE
        )

        val savedName = sharedPrefData.getString("fullName", null)
        val savedCompany = sharedPrefData.getString("companyName", null)

        view.tv_fullNameAccount.text = savedName
        view.tv_companyName.text = savedCompany

        view.tv_logout.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            logedOutSuccesfully()
            activity?.finish()
        }

        view.tv_myProfile.setOnClickListener {
            val intent = Intent(activity, CompanyProfileActivity::class.java)
            startActivity(intent)
        }
        return  view
    }

    private fun logedOutSuccesfully(){
        val sharedPref = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Login", false)
        editor.apply()
    }

}