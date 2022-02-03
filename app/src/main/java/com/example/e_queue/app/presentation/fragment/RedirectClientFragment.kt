package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.presentation.viewModel.OperationWithLoggedUserViewModel
import com.example.e_queue.databinding.FragmentRedirectClientBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RedirectClientFragment : Fragment() {

    private lateinit var binding: FragmentRedirectClientBinding
    private val operationOperationWithLoggedUserViewModel: OperationWithLoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<OperationWithLoggedUser>(Constants.OPERATION_WITH_LOGGED_USER_ARG))
    }

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

        setRedirectCustomerParams()


        binding.buttonCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.buttonRedirection.setOnClickListener {
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_container, MainFragment())
//            transaction.commit()
            //MainFragment.newInstance().
            // parentFragmentManager.popBackStack()
        }
    }

    private fun setRedirectCustomerParams() {
        operationOperationWithLoggedUserViewModel.setParams()
        operationOperationWithLoggedUserViewModel.operationWithLoggedUser.observe(viewLifecycleOwner) { loggedUser ->
            with(binding) {
                operatorName.text = loggedUser.userName
                workspaceNumber.text = loggedUser.point
                numberOfClient.text = loggedUser.clientNumber
            }
        }
    }

}