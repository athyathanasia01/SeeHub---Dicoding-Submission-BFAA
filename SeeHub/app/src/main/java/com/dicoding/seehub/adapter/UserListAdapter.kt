package com.dicoding.seehub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.databinding.ItemUserBinding

class UserListAdapter : ListAdapter<UserListResponseItem, UserListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback : OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val context = itemView.context
        fun bind(user : UserListResponseItem) {
            binding.apply {
                tvUsername.setText(user.login)
                tvId.setText(user.id.toString())
            }
            Glide.with(context)
                .load(user.avatarUrl)
                .into(binding.imgProfileUser)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserListResponseItem>() {
            override fun areItemsTheSame(
                oldItem: UserListResponseItem,
                newItem: UserListResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserListResponseItem,
                newItem: UserListResponseItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data : UserListResponseItem)
    }
}