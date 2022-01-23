package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.databinding.FragmentMainBinding
import com.example.e_queue.framework.remote.RemoteDataSource
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import kotlinx.coroutines.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var whileStart = true

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

        parentFragmentManager.setFragmentResultListener(
            "user", viewLifecycleOwner
        ) { _, bundle ->
            val loggedUser = bundle.get("loggedUser") as LoggedUser
            binding.toolbar.workspaceNumber.text = loggedUser.point
            binding.toolbar.operatorName.text = loggedUser.name
            GlobalScope.launch(Dispatchers.Main) {
                loggedUser.service_id?.let { getServiceLength(it) }
            }
        }

        binding.toolbar.exit.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, LoginFragment())
            transaction.commit()
        }

    }

    private suspend fun getServiceLength(serviceId: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            while (whileStart){
                val response = RemoteDataSource().retrofit.getUserServiceLength(serviceId)
                withContext(Dispatchers.Main) {
                    binding.quantity.amountOfClients.text = response.length.toString()
                    Log.d("pidoras","${response.length}")
                }
                delay(5000)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        whileStart = true
        Log.d("state","onResume")
    }

    override fun onPause() {
        super.onPause()
        whileStart = false
        Log.d("state","onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        whileStart = false
        Log.d("state","onDestroy")
    }


    private fun getUserInfo() {
        parentFragmentManager.setFragmentResultListener(
            "name", this
        ) { _, bundle ->
            val result = bundle.getString("UserName")

        }
    }

}