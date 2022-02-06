package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.BodyForFinishWorkWithCustomer
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.presentation.viewModel.LoggedUserViewModel
import com.example.e_queue.databinding.FragmentMainBinding
import com.example.e_queue.utils.Constants.Companion.LOGGED_USER_ARG
import com.example.e_queue.utils.Constants.Companion.OPERATION_WITH_LOGGED_USER_ARG
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val loggedUserViewModel: LoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<LoggedUser>(LOGGED_USER_ARG))
    }
    private val bundle = Bundle()

    var statusClient = 0

    var visibleButtonInviteNextCustomer = false
    var visibleButtonLookPostponedListCustomer = false
    var visibleButtonStartWorkWithCustomer = false
    var visibleButtonInviteAgainCustomer = false
    var visibleButtonNoNextCustomer = false
    var visibleButtonRedirectCustomer = false
    var visibleButtonCustomerToPostponed = false
    var visibleButtonFinishWorkWithCustomer = false
    private var visibleOneModeButtonInviteNextCustomer = false
    private var visibleOneModeButtonInviteAgainCustomer = false

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

        handleClicks()

        visibleButtonInviteNextCustomer = true
        visibleButtonLookPostponedListCustomer = true

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
        loggedUserViewModel.serviceLength.observe(viewLifecycleOwner) { serviceLength ->
            binding.quantity.amountOfClients.text = serviceLength.toString()
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

    private fun getStartWorkWithCustomer() {
        loggedUserViewModel.getStartCustomer()
    }

    private fun handleClicks() {
        loggedUserViewModel.serviceLength.observe(viewLifecycleOwner) {
            if (it == 0) {
                with(binding.someButton.buttonCallNextClient) {
                    isEnabled = false
                    background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.disable_button)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
                }
            } else {
                with(binding.someButton.buttonCallNextClient) {
                    isEnabled = true
                    background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.green_button)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }
        }
        with(binding.someButton) {
            buttonCallNextClient.setOnClickListener {
                statusClient = 1
                inviteNextCustomer()
                setInviteCustomerInfo()

                visibleButtonInviteNextCustomer = false
                visibleButtonLookPostponedListCustomer = false
                visibleButtonStartWorkWithCustomer = true
                visibleButtonInviteAgainCustomer = true
                visibleButtonNoNextCustomer = true

                changeButtonVisible()
                changeStatusClient()
            }
            buttonStartWork.setOnClickListener {
                statusClient = 2

                visibleButtonStartWorkWithCustomer = false
                visibleButtonInviteAgainCustomer = false
                visibleButtonNoNextCustomer = false
                visibleButtonRedirectCustomer = true
                visibleButtonCustomerToPostponed = true
                visibleButtonFinishWorkWithCustomer = true

                getStartWorkWithCustomer()
                changeButtonVisible()
                changeStatusClient()
            }
            buttonNoClient.setOnClickListener {
                statusClient = 0
                killNextCustomer()

                visibleButtonStartWorkWithCustomer = false
                visibleButtonInviteAgainCustomer = false
                visibleButtonNoNextCustomer = false
                visibleButtonInviteNextCustomer = true
                visibleButtonLookPostponedListCustomer = true

                changeButtonVisible()
                changeStatusClient()
            }

            buttonFinishWork.setOnClickListener {
                loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                    loggedUserViewModel.inviteNextCustomerInfo.observe(viewLifecycleOwner) {
                        if (!it.serviceName.resultRequired) {
                            val body = BodyForFinishWorkWithCustomer(
                                userId = loggedUser.id,
                                resultId = -1
                            )
                            finishWorkWithCustomer(body)
                        } else {
                            loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                                replaceFragment(
                                    fragment = ResultFragment(),
                                    userId = loggedUser.id,
                                    userName = loggedUser.name,
                                    point = loggedUser.point,
                                    clientNumber = binding.include.currentClientNumber.text.toString()
                                )
                            }
                        }
                    }
                }
            }

            buttonRedirect.setOnClickListener {
                loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                    replaceFragment(
                        fragment = RedirectClientFragment(),
                        userId = loggedUser.id,
                        userName = loggedUser.name,
                        point = loggedUser.point,
                        clientNumber = binding.include.currentClientNumber.text.toString()
                    )
                }
            }

            buttonPostpone.setOnClickListener {
                loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                    replaceFragment(
                        fragment = PostponedClientFragment(),
                        userId = loggedUser.id,
                        userName = loggedUser.name,
                        point = loggedUser.point,
                        clientNumber = binding.include.currentClientNumber.text.toString()
                    )
                }
            }
        }
    }

    private fun finishWorkWithCustomer(body: BodyForFinishWorkWithCustomer) {
        statusClient = 0

        visibleButtonRedirectCustomer = false
        visibleButtonCustomerToPostponed = false
        visibleButtonFinishWorkWithCustomer = false
        visibleButtonInviteNextCustomer = true
        visibleButtonLookPostponedListCustomer = true

        with(binding.include) {
            currentClientNumber.text = ""
            currentClientService.text = ""
        }

        changeButtonVisible()
        changeStatusClient()

        loggedUserViewModel.finishWorkWithCustomer(body)
    }

    private fun replaceFragment(
        fragment: Fragment,
        userId: Int,
        userName: String,
        point: String,
        clientNumber: String?
    ) {
        bundle.putParcelable(
            OPERATION_WITH_LOGGED_USER_ARG,
            OperationWithLoggedUser(
                userId = userId,
                userName = userName,
                point = point,
                clientNumber = clientNumber
            )
        )
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment).addToBackStack(null)
        transaction.commit()
    }

    private fun changeButtonVisible() {
        with(binding.someButton) {
            if (visibleButtonInviteNextCustomer) {
                buttonCallNextClient.visibility = View.VISIBLE
            } else {
                buttonCallNextClient.visibility = View.GONE
            }

            if (visibleButtonLookPostponedListCustomer) {
                buttonListPostponedClients.visibility = View.VISIBLE
            } else {
                buttonListPostponedClients.visibility = View.GONE
            }

            if (visibleButtonStartWorkWithCustomer) {
                buttonStartWork.visibility = View.VISIBLE
            } else {
                buttonStartWork.visibility = View.GONE
            }

            if (visibleButtonInviteAgainCustomer) {
                buttonCallNextClientAgain.visibility = View.VISIBLE
            } else {
                buttonCallNextClientAgain.visibility = View.GONE
            }

            if (visibleButtonNoNextCustomer) {
                buttonNoClient.visibility = View.VISIBLE
            } else {
                buttonNoClient.visibility = View.GONE
            }

            if (visibleButtonRedirectCustomer) {
                buttonRedirect.visibility = View.VISIBLE
            } else {
                buttonRedirect.visibility = View.GONE
            }

            if (visibleButtonCustomerToPostponed) {
                buttonPostpone.visibility = View.VISIBLE
            } else {
                buttonPostpone.visibility = View.GONE
            }

            if (visibleButtonFinishWorkWithCustomer) {
                buttonFinishWork.visibility = View.VISIBLE
            } else {
                buttonFinishWork.visibility = View.GONE
            }

            if (visibleOneModeButtonInviteNextCustomer) {
                buttonCallNextClientOneMode.visibility = View.VISIBLE
            } else {
                buttonCallNextClientOneMode.visibility = View.GONE
            }

            if (visibleOneModeButtonInviteAgainCustomer) {
                buttonCallNextClientAgainOneMode.visibility = View.VISIBLE
            } else {
                buttonCallNextClientAgainOneMode.visibility = View.GONE
            }
        }
    }

    private fun changeStatusClient() {
        with(binding.statusOfClient) {
            when (statusClient) {
                0 -> {
                    background = ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.status_client_is_not_called
                    )
                    text = getString(R.string.status_client_is_not_called)
                }
                1 -> {
                    background = ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.status_client_is_called
                    )
                    text = getString(R.string.status_client_is_called)
                }
                2 -> {
                    background = ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.status_work_with_client
                    )
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

    override fun onResume() {
        super.onResume()
        changeButtonVisible()
        setInviteCustomerInfo()
        changeStatusClient()
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