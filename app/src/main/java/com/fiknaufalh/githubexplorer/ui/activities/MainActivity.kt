package com.fiknaufalh.githubexplorer.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.adapters.UserAdapter
import com.fiknaufalh.githubexplorer.data.remote.responses.SearchResponse
import com.fiknaufalh.githubexplorer.databinding.ActivityMainBinding
import com.fiknaufalh.githubexplorer.utils.ViewModelFactory
import com.fiknaufalh.githubexplorer.viewmodels.FavoriteViewModel
import com.fiknaufalh.githubexplorer.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private val favoriteViewModel: FavoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        with (binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    mainViewModel.changeQuery(searchView.text.toString())
                    val query = mainViewModel.searchQuery.value
                    searchBar.setText(query)

                    if (query?.isEmpty()!!) {
                        mainViewModel.setSearch(false)
                        mainViewModel.findUsers("A")
                    } else {
                        mainViewModel.setSearch(true)
                        mainViewModel.findUsers(searchView.text.toString())
                    }

                    searchView.hide()
                    false
                }

            ivSwitchMode.setOnClickListener {
                switchTheme()
            }
        }

        mainViewModel.users.observe(this) {
            users -> setUserListData(users)
        }

        mainViewModel.isMainLoading.observe(this) {
            isMainLoading -> showLoading(isMainLoading)
        }
    }

    private fun setUserListData(users: SearchResponse) {
        val adapter = UserAdapter(
            onClickCard = {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(resources.getString(R.string.passing_query), it.login)
                startActivity(intent)
            })

        adapter.submitList(users.items)
        binding.rvUsers.adapter = adapter
        if (mainViewModel.isSearched.value!!) {
            binding.tvTotalResult.text =
                resources.getString(
                    R.string.search_result_text,
                    users.totalCount,
                    users.items?.size ?: 0
                )
        } else binding.tvTotalResult.text = resources.getString(R.string.search_me_text)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun switchTheme() {
//        if (binding.ivSwitchMode)
    }
}