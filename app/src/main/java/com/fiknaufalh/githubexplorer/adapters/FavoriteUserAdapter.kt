package com.fiknaufalh.githubexplorer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.data.local.entity.FavoriteUser
import com.fiknaufalh.githubexplorer.databinding.ItemUserCardBinding

class FavoriteUserAdapter(private val onClickCard: (FavoriteUser) -> Unit):
    ListAdapter<FavoriteUser, FavoriteUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserCardBinding.
        inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class MyViewHolder(val binding: ItemUserCardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteUser) {
            binding.userName.text = user.username
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .placeholder(R.drawable.account_circle)
                .into(binding.userImage)

            itemView.setOnClickListener {
                onClickCard(user)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame (oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem.username == newItem.username
            }
            override fun areContentsTheSame (oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}