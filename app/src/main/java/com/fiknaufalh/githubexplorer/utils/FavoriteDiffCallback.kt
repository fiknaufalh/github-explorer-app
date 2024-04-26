package com.fiknaufalh.githubexplorer.utils

import androidx.recyclerview.widget.DiffUtil
import com.fiknaufalh.githubexplorer.data.local.entity.FavoriteUser

class FavoriteDiffCallback(
    private val oldFavorites: List<FavoriteUser>,
    private val newFavorites: List<FavoriteUser>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldFavorites.size
    override fun getNewListSize(): Int = newFavorites.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavorites[oldItemPosition].username == newFavorites[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = oldFavorites[oldItemPosition]
        val newFavorite = newFavorites[newItemPosition]
        return oldFavorite.avatarUrl == newFavorite.avatarUrl
                && oldFavorite.htmlUrl == newFavorite.htmlUrl
    }
}