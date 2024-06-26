package com.fiknaufalh.githubexplorer.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fiknaufalh.githubexplorer.ui.fragments.FollowFragment

class FollowPagerAdapter(activity: AppCompatActivity, user: String):
    FragmentStateAdapter(activity) {

    private val username: String = user

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position + 1)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int = 2
}
