package com.fiknaufalh.githubexplorer.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.adapters.FavoriteUserAdapter
import com.fiknaufalh.githubexplorer.data.local.entity.FavoriteUser
import com.fiknaufalh.githubexplorer.databinding.ActivityFavoriteBinding
import com.fiknaufalh.githubexplorer.utils.ViewModelFactory
import com.fiknaufalh.githubexplorer.viewmodels.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        favoriteViewModel.favoriteUsers.observe(this) {
            setFavoriteUserList(it)
            favoriteViewModel.favoriteUsers.removeObservers(this)
        }

        val layoutManager = LinearLayoutManager(this)

        with (binding) {
            rvFavs.layoutManager = layoutManager
            backTab.setOnClickListener { finish() }
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.favoriteUsers.observe(this) {
            setFavoriteUserList(it)
            favoriteViewModel.favoriteUsers.removeObservers(this)
        }
    }

    private fun setFavoriteUserList(users: List<FavoriteUser>) {
        if (users.isNotEmpty()) binding.emptyList.visibility = View.GONE
        val adapter = FavoriteUserAdapter {
            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            intent.putExtra(resources.getString(R.string.passing_query), it.username)
            startActivity(intent)
        }

        adapter.submitList(users)
        binding.rvFavs.adapter = adapter
    }
}