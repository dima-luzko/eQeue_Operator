package com.example.e_queue.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_queue.app.data.model.User
import com.example.e_queue.databinding.UserItemBinding

class ChooseUserItemAdapter(
    private val userItem: List<User>,
    private val chooseName: (User) -> Unit
) :
    RecyclerView.Adapter<ChooseUserItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = userItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userItem[position]
        with(holder.binding.userName) {
            text = user.name
            setOnClickListener {
                chooseName(user)
            }
        }
    }
}