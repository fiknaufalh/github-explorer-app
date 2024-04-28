package com.fiknaufalh.githubexplorer.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.adapters.FollowPagerAdapter
import com.fiknaufalh.githubexplorer.data.local.entity.FavoriteUser
import com.fiknaufalh.githubexplorer.data.remote.responses.UserDetailResponse
import com.fiknaufalh.githubexplorer.databinding.ActivityDetailBinding
import com.fiknaufalh.githubexplorer.utils.DateConverter
import com.fiknaufalh.githubexplorer.utils.ViewModelFactory
import com.fiknaufalh.githubexplorer.viewmodels.DetailViewModel
import com.fiknaufalh.githubexplorer.viewmodels.FavoriteViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteUserData: FavoriteUser
    private val detailViewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(application)
    }
    private val favoriteViewModel by viewModels<FavoriteViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setErrorView(false)

        val userName = intent.getStringExtra(resources.getString(R.string.passing_query))

        val followPagerAdapter = FollowPagerAdapter(this, userName.toString())

        with(binding) {
            viewPager.adapter = followPagerAdapter

            fabFavorite.setOnClickListener {
                val favoriteStatus = favoriteViewModel.isFavoriteUser(favoriteUserData)
                if (favoriteStatus) {
                    favoriteViewModel.deleteFavorite(favoriteUserData)
                    fabFavorite.setImageResource(R.drawable.ic_favorite_unfilled)
                } else {
                    favoriteViewModel.insertFavorite(favoriteUserData)
                    fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
                }
            }

            backTab.setOnClickListener { finish() }
        }

        detailViewModel.userDetail.observe(this) { user ->
            setUserDetail(user)
            favoriteUserData = FavoriteUser(user.login!!, user.avatarUrl, user.htmlUrl!!)
        }

        detailViewModel.isLoading.observe(this) {
                loading -> showLoading(loading)
        }

        detailViewModel.errorToast.observe(this) {
            errorToast -> errorToast?.let {
                if (errorToast) {
                    Toast.makeText(this, "Success to retrieve the data", Toast.LENGTH_SHORT).show()
                    detailViewModel.resetToast()
                } else {
                    Toast.makeText(this, "Failed to retrieve the data", Toast.LENGTH_SHORT).show()
                    detailViewModel.resetToast()
                    setErrorView(true)
                }
            }
        }

        detailViewModel.findDetail(userName!!)

        favoriteViewModel.getFavoriteUserByUsername(userName.toString()).observe(this) {
                favoriteUser ->
            if (favoriteUser != null) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_unfilled)
            }
        }

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            val currentTab = resources.getString(TAB_TITLES[position])
            tab.text = currentTab
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setUserDetail(detail: UserDetailResponse) {
        val dateStr = detail.createdAt
        val convertedDate = DateConverter().formatDate(dateStr!!)

        with(binding) {
            tvUserName.text = detail.login
            tvFullName.text = if (detail.name.isNullOrEmpty()) resources.getString(R.string.undefinedName) else detail.name
            tvMemberSince.text = resources.getString(R.string.member_since, convertedDate)

            if (detail.location != null) tvLocation.text = detail.location.toString()
            else {
                tvLocation.visibility = View.GONE
                ivLocation.visibility = View.GONE
            }

            tvPublicReposNum.text = detail.publicRepos.toString()
            tvFollowersNum.text = detail.followers.toString()
            tvFollowingNum.text = detail.following.toString()

            ivUserProfile.borderWidth = 2
            ivUserProfile.setOnClickListener {
                val gitHubIntent = Intent(Intent.ACTION_VIEW, Uri.parse(detail.htmlUrl))
                startActivity(gitHubIntent)
            }
        }

        Glide.with(this)
            .load(detail.avatarUrl)
            .placeholder(R.drawable.account_circle)
            .into(binding.ivUserProfile)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setErrorView(isError: Boolean) {
        val isShow = if (isError) View.VISIBLE else View.GONE
        binding.ivError.visibility = isShow
        binding.tvError.visibility = isShow
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}