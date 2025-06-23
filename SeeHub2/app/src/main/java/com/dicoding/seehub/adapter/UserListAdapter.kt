package com.dicoding.seehub.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.seehub.data.database.BaseUserItem
import com.dicoding.seehub.databinding.ItemUserBinding
import com.dicoding.seehub.R

class UserListAdapter (private val onFavoriteUserClick: (BaseUserItem) -> (Unit)) : ListAdapter<BaseUserItem, UserListAdapter.MyViewHolder>(DIFF_CALLBACK) {
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
        val user = getItem(position)
        holder.bind(user, onFavoriteUserClick)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    class MyViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: BaseUserItem, onFavoriteUserClick: (BaseUserItem) -> Unit) {
            binding.tvId.text = user.id.toString()
            binding.tvUsername.text = user.login
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(binding.imgProfileUser)

            val isFavoriteUser = binding.imgHeart
            if (user.isFavorite) {
                isFavoriteUser.setImageDrawable(ContextCompat.getDrawable(isFavoriteUser.context, R.drawable.heart_red))
            } else {
                isFavoriteUser.setImageDrawable(ContextCompat.getDrawable(isFavoriteUser.context, R.drawable.heart_grey))
            }

            binding.imgHeart.setOnClickListener {
                onFavoriteUserClick(user)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: BaseUserItem)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<BaseUserItem> = object : DiffUtil.ItemCallback<BaseUserItem>() {
            override fun areItemsTheSame(
                oldItem: BaseUserItem,
                newItem: BaseUserItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: BaseUserItem,
                newItem: BaseUserItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}