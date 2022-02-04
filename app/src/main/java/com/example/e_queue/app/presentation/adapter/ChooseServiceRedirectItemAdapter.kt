package com.example.e_queue.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_queue.app.data.model.InnerServicesForServiceList
import com.example.e_queue.app.data.model.ServicesList
import com.example.e_queue.databinding.ServiceItemBinding

class ChooseServiceRedirectItemAdapter(
    private val serviceItem: ServicesList,
    private val chooseServicesName: (InnerServicesForServiceList) -> Unit
) :
    RecyclerView.Adapter<ChooseServiceRedirectItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: ServiceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ServiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = serviceItem.innerServices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = serviceItem.innerServices[position]
        with(holder.binding.serviceName) {
            text = service.name
            setOnClickListener {
                chooseServicesName(
                    InnerServicesForServiceList(
                        id = service.id,
                        name = service.name
                    )
                )
            }
        }
    }
}