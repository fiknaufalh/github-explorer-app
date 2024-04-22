package com.fiknaufalh.githubexplorer.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.adapters.UserAdapter
import com.fiknaufalh.githubexplorer.data.responses.UserItem
import com.fiknaufalh.githubexplorer.databinding.FragmentFollowBinding
import com.fiknaufalh.githubexplorer.ui.activities.DetailActivity
import com.fiknaufalh.githubexplorer.viewmodels.DetailViewModel


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var detailViewModel: DetailViewModel

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

        detailViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]

        detailViewModel.followList.observe(viewLifecycleOwner) {
                list -> setFollowListData(list)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
                loading -> showLoading(loading)
        }

        val layoutManager = LinearLayoutManager(this.context)
        binding.rvFollows.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this.context, layoutManager.orientation)
        binding.rvFollows.addItemDecoration(itemDecoration)

        val type = if (position == 1) "followers" else "following"
        detailViewModel.getFollow(type, username!!)
    }

    private fun setFollowListData(list: List<UserItem>) {
        binding.tvTotalFollow.text = resources.getString(R.string.follow_result, list.size)
        if (list.isNotEmpty()) {
            val adapter = UserAdapter(list, onClickCard = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("q", it.login)
                startActivity(intent)
            })
            binding.rvFollows.adapter = adapter
        } else {
            binding.emptyList.text = getString(R.string.empty_list)
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