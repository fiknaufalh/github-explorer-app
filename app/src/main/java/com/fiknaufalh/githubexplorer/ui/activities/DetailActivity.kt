package com.fiknaufalh.githubexplorer.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        val userName = intent.getStringExtra(resources.getString(R.string.passing_query))

        detailViewModel.userDetail.observe(this) { user ->
            setUserDetail(user)
            favoriteViewModel.favoriteUsers.observe(this) {
                binding.fabFavorite.setOnClickListener {
                    val favoriteUser = FavoriteUser()
                    favoriteUser.username = user.login!!
                    favoriteUser.avatarUrl = user.avatarUrl
                    favoriteUser.htmlUrl = user.htmlUrl!!

                    if (favoriteViewModel.isFavorite(favoriteUser)) {
                        favoriteViewModel.deleteFavorite(favoriteUser)
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_unfilled)

                    } else {
                        favoriteViewModel.insertFavorite(favoriteUser)
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
                    }
                }
            }

            val followPagerAdapter = user.login?.let { it -> FollowPagerAdapter(this, it) }
            binding.viewPager.adapter = followPagerAdapter

            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                val currentTab = resources.getString(TAB_TITLES[position])
                val count = if (currentTab == resources.getString(R.string.tab_followers)) {
                    detailViewModel.userDetail.value?.followers
                } else {
                    detailViewModel.userDetail.value?.following
                }

                tab.text = resources.getString(R.string.tab_text, count, currentTab)
            }.attach()

            supportActionBar?.elevation = 0f
        }

        detailViewModel.isLoading.observe(this) {
            loading -> showLoading(loading)
        }

        detailViewModel.findDetail(userName!!)

        binding.backTab.setOnClickListener { finish() }


    }

    private fun setUserDetail(detail: UserDetailResponse) {
        val dateStr = detail.createdAt
        val convertedDate = DateConverter().formatDate(dateStr!!)

        with(binding) {
            tvUserName.text = detail.login
            tvFullName.text = if (detail.name.isNullOrEmpty()) resources.getString(R.string.undefinedName) else detail.name
            tvMemberSince.text = resources.getString(R.string.member_since, convertedDate)
            if (detail.email != null) {
                tvEmail.text = detail.email.toString()
            } else {
                tvEmail.visibility = View.GONE
            }
//            ivUserProfile.borderColor = resources.getColor(R.color.white)
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

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}