package com.sizdev.arkhireforcompany.homepage.profile.talent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.databinding.FragmentWorkExperienceBinding
import com.sizdev.arkhirefortalent.homepage.profile.WorkExperienceAdapter


class WorkExperienceFragment : Fragment() {

    private lateinit var binding: FragmentWorkExperienceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_experience, container, false)
        binding.rvTalentWorkExperience.adapter = WorkExperienceAdapter()
        binding.rvTalentWorkExperience.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)


        return binding.root
    }

}