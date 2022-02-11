package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_queue.R
import com.example.e_queue.app.data.model.BodyForFinishWorkWithCustomer
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.presentation.adapter.ChooseResultsAdapter
import com.example.e_queue.app.presentation.viewModel.OperationWithLoggedUserViewModel
import com.example.e_queue.app.presentation.viewModel.ResultsListViewModel
import com.example.e_queue.databinding.FragmentResultBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import com.example.e_queue.utils.snackBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val operationOperationWithLoggedUserViewModel: OperationWithLoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<OperationWithLoggedUser>(Constants.OPERATION_WITH_LOGGED_USER_ARG))
    }
    private val resultsListViewModel by viewModel<ResultsListViewModel>()
    private val bundle = Bundle()

    private var selectedResults = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
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
        getResultList()
        finishWork()
    }

    private fun finishWork() {
        binding.buttonOk.setOnClickListener {
            if (!selectedResults) {
                snackBar(requireView(), requireContext(), R.string.choose_results_snack_bar)
            } else {
                finishWorkWithCustomer()
            }
        }
    }

    private fun finishWorkWithCustomer() {
        PreferencesManager.getInstance(requireContext())
            .putBoolean(PreferencesManager.PREF_FLAG, false)
        operationOperationWithLoggedUserViewModel.operationWithLoggedUser.observe(viewLifecycleOwner) { loggedUser ->
            resultsListViewModel.selectedResults.observe(viewLifecycleOwner) { result ->
                val body = BodyForFinishWorkWithCustomer(
                    userId = loggedUser.userId,
                    resultId = result
                )
                operationOperationWithLoggedUserViewModel.finishWorkWithCustomer(body)
                replaceFragment(
                    MainFragment(),
                    loggedUser.userId,
                    loggedUser.userName,
                    loggedUser.point
                )
            }
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

    private fun getResultList() {
        with(resultsListViewModel) {
            getResultsList()
            results.observe(viewLifecycleOwner) { observeResultsList ->
                with(binding.chooseResultList) {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = ChooseResultsAdapter(
                        observeResultsList
                    ) { resultsAdapter ->
                        resultsListViewModel.getSelectedResultsId(resultsAdapter)
                        this@ResultFragment.selectedResults = true
                    }
                    hasFixedSize()
                }
            }
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

    override fun onStart() {
        super.onStart()
        selectedResults = false
    }
}