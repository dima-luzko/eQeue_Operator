package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_queue.R
import com.example.e_queue.app.data.model.InvitePostponedClient
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.presentation.adapter.ChoosePostponedClientAdapter
import com.example.e_queue.app.presentation.viewModel.OperationWithLoggedUserViewModel
import com.example.e_queue.app.presentation.viewModel.PostponedListViewModel
import com.example.e_queue.databinding.FragmentPostponedClientsListBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import com.example.e_queue.utils.snackBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PostponedClientListFragment : Fragment() {

    private lateinit var binding: FragmentPostponedClientsListBinding
    private val operationOperationWithLoggedUserViewModel: OperationWithLoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<OperationWithLoggedUser>(Constants.OPERATION_WITH_LOGGED_USER_ARG))
    }
    private val postponedListViewModel by viewModel<PostponedListViewModel>()
    private val bundle = Bundle()
    private var selectedResults = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostponedClientsListBinding.inflate(inflater, container, false)
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
        setPostponedClientLength()
        getPostponedClientList()
        handleClick()
    }

    private fun setCustomerParams() {
        operationOperationWithLoggedUserViewModel.setParams()
        operationOperationWithLoggedUserViewModel.operationWithLoggedUser.observe(viewLifecycleOwner) { loggedUser ->
            with(binding) {
                operatorName.text = loggedUser.userName
                workspaceNumber.text = loggedUser.point
            }
        }
    }

    private fun setPostponedClientLength() {
        with(postponedListViewModel) {
            setPostponedLength()
            postponedClientLength.observe(viewLifecycleOwner) {
                binding.amountOfClients.text =
                    getString(R.string.amount_of_postponed_clients1, it)
            }
        }

    }

    private fun replaceFragment(
        fragment: Fragment,
        id: Int,
        name: String,
        point: String,
        number: String?,
        serviceName: String?,
        prefix: String?,
        resultRequired: Boolean
    ) {
        bundle.putParcelable(
            Constants.LOGGED_USER_ARG,
            LoggedUser(
                id = id,
                name = name,
                point = point
            )
        )
        bundle.putParcelable(
            Constants.INVITE_POSTPONED_ARG,
            InvitePostponedClient(
                number = number,
                serviceName = serviceName,
                prefix = prefix,
                resultRequired = resultRequired
            )
        )
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun handleClick() {
        postponedListViewModel.postponedClientLength.observe(viewLifecycleOwner) {
            with(binding.buttonInviteClient) {
                if (it == "0") {
                    isEnabled = false
                    background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.disable_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_text
                        )
                    )
                } else {
                    isEnabled = true
                    background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.green_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
        }
        with(binding) {
            buttonCancel.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            buttonInviteClient.setOnClickListener {
                if (!selectedResults) {
                    snackBar(requireView(), requireContext(), R.string.choose_client_snack_bar)
                } else {
                    invitePostponedCustomers()
                }
            }
        }
    }

    private fun invitePostponedCustomers() {
        PreferencesManager.getInstance(requireContext())
            .putBoolean(PreferencesManager.PREF_FLAG, true)
        PreferencesManager.getInstance(requireContext())
            .putBoolean(PreferencesManager.PREF_POSTPONED_CUSTOMER, false)
        operationOperationWithLoggedUserViewModel.operationWithLoggedUser.observe(viewLifecycleOwner) { loggedUser ->
            postponedListViewModel.selectedClientId.observe(viewLifecycleOwner) { selectedClientId ->
                postponedListViewModel.invitePostponedCustomer(loggedUser.userId, selectedClientId)
            }
            postponedListViewModel.invitePostponedCustomer.observe(viewLifecycleOwner) { postponedClient ->
                replaceFragment(
                    MainFragment(),
                    loggedUser.userId,
                    loggedUser.userName,
                    loggedUser.point,
                    postponedClient.number,
                    postponedClient.serviceName.name,
                    postponedClient.prefix,
                    postponedClient.serviceName.resultRequired
                )
            }
        }
    }

    private fun getPostponedClientList() {
        with(postponedListViewModel) {
            getPostponedClientList()
            results.observe(viewLifecycleOwner) { observeList ->
                with(binding.postponedClientList) {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = ChoosePostponedClientAdapter(
                        requireContext(),
                        observeList
                    ) { resultAdapter ->
                        getSelectedResultsId(resultAdapter.id)
                        this@PostponedClientListFragment.selectedResults = true
                    }
                    hasFixedSize()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        selectedResults = false
    }
}
