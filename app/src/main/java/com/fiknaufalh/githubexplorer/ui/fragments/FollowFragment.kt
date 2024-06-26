package com.fiknaufalh.githubexplorer.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.adapters.UserAdapter
import com.fiknaufalh.githubexplorer.data.remote.responses.UserItem
import com.fiknaufalh.githubexplorer.databinding.FragmentFollowBinding
import com.fiknaufalh.githubexplorer.ui.activities.DetailActivity
import com.fiknaufalh.githubexplorer.utils.ViewModelFactory
import com.fiknaufalh.githubexplorer.viewmodels.DetailViewModel


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val detailViewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var position: Int? = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        detailViewModel.followersList.observe(viewLifecycleOwner) {
            list -> setFollowListData(list)
        }

        detailViewModel.followingList.observe(viewLifecycleOwner) {
            list -> setFollowListData(list)
        }

        detailViewModel.isDetailLoading.observe(viewLifecycleOwner) {
            loading -> showLoading(loading)
        }

        val layoutManager = LinearLayoutManager(this.context)
        val itemDecoration = DividerItemDecoration(this.context, layoutManager.orientation)

        with (binding) {
            rvFollows.layoutManager = layoutManager
            rvFollows.addItemDecoration(itemDecoration)
        }

        if (position == 1) detailViewModel.findFollowers(username!!)
        else detailViewModel.findFollowing(username!!)
    }

    private fun setFollowListData(list: List<UserItem>) {
        with(binding) {
            tvTotalFollow.text = resources.getString(R.string.follow_result, list.size)

            if (list.isEmpty()) emptyList.text = getString(R.string.empty_list)
            else {
                val adapter = UserAdapter(onClickCard = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("q", it.login)
                    startActivity(intent)
                })
                adapter.submitList(list)
                rvFollows.adapter = adapter
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_USERNAME: String = "username"
        const val ARG_POSITION: String = "position"
    }
}