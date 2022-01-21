package com.example.e_queue.app.presentation.fragment

import android.annotation.SuppressLint
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
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var mStompClient: StompClient? = null

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
        }

        binding.toolbar.exit.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, LoginFragment())
            transaction.commit()
        }

    }


    private fun getUserInfo() {
        parentFragmentManager.setFragmentResultListener(
            "name", this
        ) { _, bundle ->
            val result = bundle.getString("UserName")
            Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
        }
    }

}