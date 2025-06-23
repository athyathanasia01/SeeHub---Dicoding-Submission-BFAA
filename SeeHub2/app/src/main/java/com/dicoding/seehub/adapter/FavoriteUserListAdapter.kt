package com.dicoding.seehub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.seehub.data.database.UserFavoriteEntity
import com.dicoding.seehub.databinding.ItemUserBinding
import com.dicoding.seehub.R
import com.dicoding.seehub.adapter.FavoriteUserListAdapter.MyViewHolder

class FavoriteUserListAdapter (private val onUnfavClick: (UserFavoriteEntity) -> (Unit)) : ListAdapter<UserFavoriteEntity, MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val userFavorite = getItem(position)
        holder.bind(userFavorite, onUnfavClick)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(userFavorite)
        }
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFavoriteEntity, onUnfavClick: (UserFavoriteEntity) -> Unit) {
            binding.tvId.text = user.id.toString()
            binding.tvUsername.text = user.login
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(binding.imgProfileUser)

            binding.imgHeart.setImageDrawable(ContextCompat.getDrawable(binding.imgHeart.context, R.drawable.heart_red))
            binding.imgHeart.setOnClickListener {
                onUnfavClick(user)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserFavoriteEntity)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserFavoriteEntity> = object : DiffUtil.ItemCallback<UserFavoriteEntity>() {
            override fun areItemsTheSame(
                oldItem: UserFavoriteEntity,
                newItem: UserFavoriteEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UserFavoriteEntity,
                newItem: UserFavoriteEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}