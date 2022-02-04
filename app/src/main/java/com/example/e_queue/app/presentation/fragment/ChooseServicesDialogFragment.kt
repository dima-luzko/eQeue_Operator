package com.example.e_queue.app.presentation.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_queue.app.data.model.SelectedServices
import com.example.e_queue.app.presentation.adapter.ChooseServiceRedirectItemAdapter
import com.example.e_queue.app.presentation.viewModel.ServicesListViewModel
import com.example.e_queue.databinding.FragmentChooseRedirectClientDialogBinding
import com.example.e_queue.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseServicesDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentChooseRedirectClientDialogBinding
    private var bundle = Bundle()
    private val servicesViewModel by viewModel<ServicesListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseRedirectClientDialogBinding.inflate(inflater, container, false)
        dialog?.window?.let {
            with(it) {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getServicesList()
    }

    private fun getServicesList() {
        with(servicesViewModel) {
            getServicesList()
            services.observe(viewLifecycleOwner) { observeServicesList ->
                with(binding.chooseServiceList) {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = ChooseServiceRedirectItemAdapter(
                        observeServicesList
                    ) { servicesAdapter ->
                        bundle.putParcelable(
                            Constants.SELECTED_SERVICES_ARG, SelectedServices(
                                id = servicesAdapter.id,
                                name = servicesAdapter.name
                            )
                        )
                        parentFragmentManager.setFragmentResult(Constants.SELECTED_SERVICE_REQUEST_KEY, bundle)
                        dialog?.dismiss()
                    }
                    hasFixedSize()
                }
            }

        }
    }

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            val fragment = ChooseServicesDialogFragment()
            fragment.show(
                fragmentManager,
                ChooseServicesDialogFragment::class.java.simpleName
            )
        }
    }

}