package com.example.e_queue.app.presentation.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_queue.R
import com.example.e_queue.app.data.model.InviteNextCustomerInfo
import com.example.e_queue.databinding.PostponedClientsItemBinding

class ChoosePostponedClientAdapter (
    private val context: Context,
    private val clientItem: List<InviteNextCustomerInfo>,
    private val chooseClientId: (InviteNextCustomerInfo) -> Unit
) :
    RecyclerView.Adapter<ChoosePostponedClientAdapter.ViewHolder>() {

    private var rowIndex = -1

    class ViewHolder(val binding: PostponedClientsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            PostponedClientsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = clientItem.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val results = clientItem[position]
        with(holder.binding) {
            val layoutParams: ViewGroup.LayoutParams = card.layoutParams
            clientNumber.text = results.prefix + results.number + " - " + results.serviceName.name
            comment.text = results.comments
            card.setOnClickListener {
                rowIndex = position
                notifyDataSetChanged()
                chooseClientId(
                    results
                )

            }

            if (rowIndex == position) {
                clientNumber.setTextColor(ContextCompat.getColor(context, R.color.white))
                comment.setTextColor(ContextCompat.getColor(context, R.color.white))
                card.background =
                    ContextCompat.getDrawable(context, R.drawable.active_background_for_postponed_list)
//                layoutParams.width = R.dimen._220mdp
//                card.layoutParams = layoutParams
            } else {
                clientNumber.setTextColor(ContextCompat.getColor(context, R.color.gray))
                comment.setTextColor(ContextCompat.getColor(context, R.color.gray))
                card.background = ContextCompat.getDrawable(context, R.drawable.background_for_postponed_list)
//                layoutParams.width = R.dimen._200mdp
//                card.layoutParams = layoutParams
            }
        }
    }
}
