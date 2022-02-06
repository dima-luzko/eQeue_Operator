package com.example.e_queue.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_queue.R
import com.example.e_queue.app.data.model.ResultList
import com.example.e_queue.databinding.ResultItemBinding

class ChooseResultsAdapter(
    private val resultsItem: ResultList,
    private val chooseResultsId: (Int) -> Unit
) :
    RecyclerView.Adapter<ChooseResultsAdapter.ViewHolder>() {

    private var rowIndex = -1

    class ViewHolder(val binding: ResultItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = resultsItem.result.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val results = resultsItem.result[position]
        with(holder.binding.item) {
            text = results.name
            setOnClickListener {
                rowIndex = position
                notifyDataSetChanged()
                chooseResultsId(
                    results.id
                )

            }

            if (rowIndex == position) {
                setTextColor(ContextCompat.getColor(context, R.color.white))
                background =
                    ContextCompat.getDrawable(context, R.drawable.selected_img_for_result_list)
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.user_item_text))
                background = ContextCompat.getDrawable(context, R.drawable.img_for_result_list)
            }
        }
    }
}