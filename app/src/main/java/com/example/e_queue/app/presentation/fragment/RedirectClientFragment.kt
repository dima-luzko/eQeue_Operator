package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.BodyForRedirectCustomer
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.data.model.SelectedServices
import com.example.e_queue.app.presentation.fragment.dialog.ChooseServicesDialogFragment
import com.example.e_queue.app.presentation.viewModel.OperationWithLoggedUserViewModel
import com.example.e_queue.app.presentation.viewModel.SelectedServicesViewModel
import com.example.e_queue.databinding.FragmentRedirectClientBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import com.example.e_queue.utils.snackBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RedirectClientFragment : Fragment() {

    private lateinit var binding: FragmentRedirectClientBinding
    private val operationOperationWithLoggedUserViewModel: OperationWithLoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<OperationWithLoggedUser>(Constants.OPERATION_WITH_LOGGED_USER_ARG))
    }
    private val selectedServicesViewModel by viewModel<SelectedServicesViewModel>()
    private val bundle = Bundle()

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
        setServiceName()
        handleClick()
    }

    private fun handleClick() {
        with(binding) {
            buttonCancel.setOnClickListener {
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_ON_BACK_PRESSED, true)
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_DONT_PLAY_SOUND, true)
                parentFragmentManager.popBackStack()
            }
            buttonRedirection.setOnClickListener {
                if (chooseServiceName.text == getString(R.string.service_name)) {
                    snackBar(requireView(), requireContext(), R.string.choose_service_snack_bar)
                } else {
                    PreferencesManager.getInstance(requireContext())
                        .putBoolean(PreferencesManager.PREF_REDIRECT_CUSTOMER, true)
                    redirectCustomer()
                }
            }
        }
    }

    private fun setServiceName() {
        selectedServicesViewModel.services.observe(viewLifecycleOwner) {
            binding.chooseServiceName.text = it.name
        }
    }

    private fun chooseService() {
        binding.inputService.setOnClickListener {
            ChooseServicesDialogFragment.showDialog(parentFragmentManager)
        }
    }

    private fun replaceFragment(
        fragment: Fragment,
        id: Int,
        name: String,
        point: String
    ) {
        bundle.putParcelable(
            Constants.LOGGED_USER_ARG,
            LoggedUser(
                id = id,
                name = name,
                point = point
            )
        )
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun redirectCustomer() {
        operationOperationWithLoggedUserViewModel.operationWithLoggedUser.observe(viewLifecycleOwner) { loggedUser ->
            selectedServicesViewModel.services.observe(viewLifecycleOwner) { service ->
                val body = BodyForRedirectCustomer(
                    serviceId = service.id,
                    userId = loggedUser.userId,
                    comments = binding.inputPostponingComment.text.toString(),
                    resultId = 1,
                    requestBack = binding.checkboxWithReturnClient.isChecked
                )
                operationOperationWithLoggedUserViewModel.redirectCustomer(body)
            }
            replaceFragment(
                MainFragment(),
                loggedUser.userId,
                loggedUser.userName,
                loggedUser.point
            )
        }
    }

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