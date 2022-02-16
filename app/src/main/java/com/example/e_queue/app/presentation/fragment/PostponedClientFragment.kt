package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.BodyForPostponedCustomer
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.presentation.viewModel.OperationWithLoggedUserViewModel
import com.example.e_queue.databinding.FragmentPostponedClientBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PostponedClientFragment : Fragment() {

    private lateinit var binding: FragmentPostponedClientBinding
    private val operationOperationWithLoggedUserViewModel: OperationWithLoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<OperationWithLoggedUser>(Constants.OPERATION_WITH_LOGGED_USER_ARG))
    }
    private val bundle = Bundle()
    private var timePostponedClient = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostponedClientBinding.inflate(inflater, container, false)
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
        setCustomerParams()
        handleClick()
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

    private fun postponedCustomer() {
        operationOperationWithLoggedUserViewModel.operationWithLoggedUser.observe(viewLifecycleOwner) { loggedUser ->
            val body = BodyForPostponedCustomer(
                userId = loggedUser.userId,
                comments = binding.inputPostponingComment.text.toString(),
                isOnlyMine = binding.checkboxOnlyForMe.isChecked,
                postponedPeriod = timePostponedClient
            )
            operationOperationWithLoggedUserViewModel.customerToPostpone(body)
            replaceFragment(
                MainFragment(),
                loggedUser.userId,
                loggedUser.userName,
                loggedUser.point
            )
        }
    }

    private fun handleClick() {
        with(binding) {
            buttonCancel.setOnClickListener {
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_ON_BACK_PRESSED, true)
                requireActivity().onBackPressed()
            }
            buttonPostponing.setOnClickListener {
                postponedCustomer()
            }
            plus.setOnClickListener {
                changeTimePlus()
            }
            minus.setOnClickListener {
                changeTimeMinus()
            }
        }
    }

    private fun changeTimePlus() {
        if (timePostponedClient < 60) {
            timePostponedClient += 5
        } else {
            timePostponedClient
        }
        binding.postponedPeriod.text = timePostponedClient.toString()
    }

    private fun changeTimeMinus() {
        if (timePostponedClient > 0) {
            timePostponedClient -= 5
        } else {
            timePostponedClient
        }
        binding.postponedPeriod.text = timePostponedClient.toString()
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
}