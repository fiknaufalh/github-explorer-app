package com.fiknaufalh.githubexplorer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.data.responses.UserItem
import com.fiknaufalh.githubexplorer.databinding.ItemUserSearchBinding

class UserAdapter(
    private val listUser: List<UserItem>,
    private val onClickCard: (UserItem) -> Unit
): RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        val binding = ItemUserSearchBinding.
        inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }

    inner class MyViewHolder(val binding: ItemUserSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItem) {

            itemView.setOnClickListener {
                onClickCard(user)
            }

            binding.userName.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .placeholder(R.drawable.account_circle)
                .into(binding.userImage)
        }
    }

}