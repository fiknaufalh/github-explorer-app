package com.fiknaufalh.githubexplorer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.adapters.UserAdapter.MyViewHolder
import com.fiknaufalh.githubexplorer.data.responses.UserItem
import com.fiknaufalh.githubexplorer.databinding.ItemUserSearchBinding

class UserAdapter(private val onClickCard: (UserItem) -> Unit):
    ListAdapter<UserItem, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserSearchBinding.
        inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class MyViewHolder(val binding: ItemUserSearchBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItem) {
            binding.userName.text = user.login
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame (oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame (oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}