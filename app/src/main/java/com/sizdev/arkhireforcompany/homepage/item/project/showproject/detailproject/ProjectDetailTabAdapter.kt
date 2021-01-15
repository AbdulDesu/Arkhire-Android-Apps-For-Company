package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.contributor.ProjectContributorFragment
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.details.ProjectDetailFragment
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist.ProjectHiringFragment

class ProjectDetailTabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    val fragment = arrayOf(ProjectDetailFragment(), ProjectContributorFragment(), ProjectHiringFragment())

    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Project"
            1 -> "Contributor"
            2 -> "Hiring"
            else -> ""
        }
    }
}