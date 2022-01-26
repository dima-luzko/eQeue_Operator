package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.presentation.viewModel.LoggedUserViewModel
import com.example.e_queue.databinding.FragmentMainBinding
import com.example.e_queue.utils.Constants.Companion.LOGGED_USER_ARG
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val loggedUserViewModel: LoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<LoggedUser>(LOGGED_USER_ARG))
    }

    private var statusClient = 0

    companion object {
        fun newInstance(loggedUserModel: LoggedUser) = MainFragment().apply {
            arguments = bundleOf(LOGGED_USER_ARG to loggedUserModel)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
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
        setToolbarText()
        setServiceLength()
        setNextCustomer()
        changeStatusClient()
        handleClicks()
        unLoggedUser()
    }

    private fun setToolbarText() {
        loggedUserViewModel.setUserParams()
        loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) {
            with(binding.toolbar) {
                operatorName.text = it.name
                workspaceNumber.text = it.point
            }
        }
    }

    private fun unLoggedUser() {
        binding.toolbar.exit.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, LoginFragment())
            transaction.commit()
        }
    }

    private fun setServiceLength() {
        loggedUserViewModel.serviceLength.observe(viewLifecycleOwner) {
            binding.quantity.amountOfClients.text =
                if (it.length == 0) "0" else it.length.toString()
        }
    }

    private fun setNextCustomer() {
        loggedUserViewModel.nextCustomerInfo.observe(viewLifecycleOwner) {
            binding.nextClientNumber.text = (it.prefix + it.number)
            binding.nextClientArrow.visibility =
                if (binding.nextClientNumber.text.isEmpty()) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun inviteNextCustomer() {
        loggedUserViewModel.inviteNextCustomer()
    }

    private fun killNextCustomer() {
        loggedUserViewModel.killNextCustomer()
        with(binding.include) {
            currentClientNumber.text = ""
            currentClientService.text = ""
        }
    }

    private fun handleClicks() {
//        loggedUserViewModel.serviceLength.observe(viewLifecycleOwner) {
//            if (it.length == 0) {
//                with(binding.someButton.buttonCallNextClient){
//                    isEnabled = false
//                    background = ContextCompat.getDrawable(requireActivity(),R.drawable.disable_button)
//                    setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_text))
//                }
//            } else {
//                with(binding.someButton.buttonCallNextClient){
//                    isEnabled = true
//                    background = ContextCompat.getDrawable(requireActivity(), R.drawable.green_button)
//                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//                }
//            }
//        }
        with(binding.someButton) {
            buttonCallNextClient.setOnClickListener {
                statusClient = 1
                inviteNextCustomer()
                setInviteCustomerInfo()
            }
            buttonNoClient.setOnClickListener {
                statusClient = 0
                killNextCustomer()
            }
        }
    }

    private fun changeStatusClient(){
        with(binding.statusOfClient){
            when(statusClient){
                0 -> {
                    background = ContextCompat.getDrawable(requireActivity(),R.drawable.status_client_is_not_called)
                    text = getString(R.string.status_client_is_not_called)
                }
                1 -> {
                    background = ContextCompat.getDrawable(requireActivity(),R.drawable.status_client_is_called)
                    text = getString(R.string.status_client_is_called)
                }
                2 -> {
                    background = ContextCompat.getDrawable(requireActivity(),R.drawable.status_work_with_client)
                    text = getString(R.string.status_work_with_client)
                }
            }
        }
    }

    private fun setInviteCustomerInfo() {
        loggedUserViewModel.inviteNextCustomerInfo.observe(viewLifecycleOwner) {
            with(binding.include) {
                currentClientNumber.text = (it.prefix + it.number)
                currentClientService.text = it.serviceName.name
            }
        }
    }

    override fun onStart() {
        super.onStart()
        with(loggedUserViewModel) {
            startGetServiceLength()
            startGetNextCustomerInfo()
        }
    }

    override fun onStop() {
        super.onStop()
        with(loggedUserViewModel) {
            stopGetServiceLength()
            stopGetNextCustomerInfo()
        }
    }

}