package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.presentation.viewModel.LoggedUserViewModel
import com.example.e_queue.app.presentation.viewModel.SelectedUserViewModel
import com.example.e_queue.databinding.FragmentMainBinding
import com.example.e_queue.framework.remote.RemoteDataSource
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val loggedUserViewModel by viewModel<LoggedUserViewModel>()

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

        setUserInfoInViewModel()
        setToolbarText()
//        getServiceLength()
//        setServiceLength()
        unLoggedUser()

    }

    private fun setToolbarText() {
        loggedUserViewModel.loggedUser.observe(viewLifecycleOwner){
            with(binding.toolbar) {
                operatorName.text = it.name
                workspaceNumber.text = it.point
            }
        }
    }

    private fun setServiceLength(){
        loggedUserViewModel.serviceLength.observe(viewLifecycleOwner){
            binding.quantity.amountOfClients.text = it.length.toString()
        }
    }



    private fun unLoggedUser() {
        binding.toolbar.exit.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, LoginFragment())
            transaction.commit()
        }
    }

    private fun getServiceLength() {
        loggedUserViewModel.loggedUser.observe(viewLifecycleOwner){
            loggedUserViewModel.test(it.service_id)
        }

    }

    override fun onResume() {
        super.onResume()
        loggedUserViewModel.pause = false
        Log.d("state", "onResume")
    }

    override fun onPause() {
        super.onPause()
        loggedUserViewModel.pause = true
        Log.d("state", "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        loggedUserViewModel.pause = true
        Log.d("state", "onDestroy")
    }


    private fun setUserInfoInViewModel() {
        val loggedUser = arguments?.getParcelable<LoggedUser>("loggedUser")
        loggedUserViewModel

    }

}