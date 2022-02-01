package com.example.ktorclientsample.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ktorclientsample.databinding.UserItemBinding
import com.example.ktorclientsample.domain.data.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val root = UserItemBinding.inflate(layoutInflater)
        return UserViewHolder(root)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (users.isNotEmpty()) {
            users[position].let {
                holder.name.text = it.name
                holder.email.text = it.email
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun submitUsers(newUsers: List<User>) {
        if (newUsers.isNotEmpty()) {
            users.clear()
            users.addAll(newUsers)
            notifyItemRangeChanged(0, newUsers.size)
        }
    }


    inner class UserViewHolder(binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val email: TextView = binding.itemEmail
    }

}