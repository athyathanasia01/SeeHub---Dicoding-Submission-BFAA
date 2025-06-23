package com.dicoding.seehub.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.seehub.ui.FollowFragment

class SectionsPagerAdapter internal constructor(activity : AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        val bundle = Bundle()
        if (position == 0) {
            bundle.putString(FollowFragment.ARG_TAB, FollowFragment.TAB_FOLLOWERS)
        } else {
            bundle.putString(FollowFragment.ARG_TAB, FollowFragment.TAB_FOLLOWING)
        }

        fragment.arguments = bundle
        return fragment
    }
}