package com.niloythings.kittask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.niloythings.kittask.databinding.ItemUserBinding
import com.niloythings.kittask.entities.User

class UserAdapter(private val onUserItemClick: (User) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    var userList: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemUserBinding = DataBindingUtil.inflate(
            inflater, R.layout.item_user, parent, false
        )
        return UserViewHolder(binding, onUserItemClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(
        private val binding: ItemUserBinding,
        private val onUserItemClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.user = user
            binding.root.setOnClickListener { onUserItemClick.invoke(user) }
            binding.executePendingBindings()
        }
    }
}