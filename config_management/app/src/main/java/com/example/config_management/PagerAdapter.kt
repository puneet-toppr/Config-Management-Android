package com.example.config_management

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var title = arrayListOf<String>()

    override fun getItem(position: Int): Fragment {
        return EntityFragment.newInstance(title[position])
    }
    override fun getCount(): Int {
        return title.size
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

}