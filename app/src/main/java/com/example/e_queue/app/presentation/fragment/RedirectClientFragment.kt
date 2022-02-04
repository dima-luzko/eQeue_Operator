package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.BodyForRedirectCustomer
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.data.model.SelectedServices
import com.example.e_queue.app.presentation.viewModel.OperationWithLoggedUserViewModel
import com.example.e_queue.app.presentation.viewModel.SelectedServicesViewModel
import com.example.e_queue.databinding.FragmentRedirectClientBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

class RedirectClientFragment : Fragment() {

    private lateinit var binding: FragmentRedirectClientBinding
    private val operationOperationWithLoggedUserViewModel: OperationWithLoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<OperationWithLoggedUser>(Constants.OPERATION_WITH_LOGGED_USER_ARG))
    }
    private val selectedServicesViewModel by viewModel<SelectedServicesViewModel>()

//    companion object {
//        fun newInstance(operationWithLoggedUser: OperationWithLoggedUser) = MainFragment().apply {
//            arguments = bundleOf(Constants.OPERATION_WITH_LOGGED_USER_ARG to operationWithLoggedUser)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRedirectClientBinding.inflate(inflater, container, false)
        changeBackgroundAndNavBarColor(requireActivity(), R.color.gray_background)
        requireActivity().window.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.bg_gradient
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSelectedServiceInViewModel()
        setCustomerParams()
        chooseService()


        binding.buttonCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.buttonRedirection.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, MainFragment())
            transaction.commit()
        }

        selectedServicesViewModel.services.observe(viewLifecycleOwner){
            binding.chooseServiceName.text = it.name

        }
    }

    private fun chooseService() {
        binding.inputService.setOnClickListener {
            ChooseServicesDialogFragment.showDialog(parentFragmentManager)
        }
    }

//    private fun redirectCustomer(){
//        selectedServicesViewModel.services.observe(viewLifecycleOwner){
//            val body = BodyForRedirectCustomer(
//                serviceId = it.id
//
//            )
//
//        }
//
//    }

    private fun setCustomerParams() {
        operationOperationWithLoggedUserViewModel.setParams()
        operationOperationWithLoggedUserViewModel.operationWithLoggedUser.observe(viewLifecycleOwner) { loggedUser ->
            with(binding) {
                operatorName.text = loggedUser.userName
                workspaceNumber.text = loggedUser.point
                numberOfClient.text = loggedUser.clientNumber
            }
        }
    }

    private fun setSelectedServiceInViewModel() {
        parentFragmentManager.setFragmentResultListener(
            Constants.SELECTED_SERVICE_REQUEST_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val service = bundle.get(Constants.SELECTED_SERVICES_ARG) as SelectedServices
            selectedServicesViewModel.setServices(service)
        }
    }

}