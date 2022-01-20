package com.example.e_queue.app.presentation.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.presentation.adapter.ChooseUserItemAdapter
import com.example.e_queue.databinding.FragmentChooseUserDialogBinding
import com.example.e_queue.framework.remote.RemoteDataSource
import kotlinx.coroutines.*


class ChooseUserDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentChooseUserDialogBinding
    private var bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseUserDialogBinding.inflate(inflater, container, false)
        dialog?.window?.let {
            with(it) {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserList()
    }

    private fun setUserList() {
        GlobalScope.launch(Dispatchers.Main) {
            with(binding.chooseUserList) {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = ChooseUserItemAdapter(
                    userList()
                ) {
                    bundle.putSerializable("data",LoggedUser(id =it.id,name = it.name, point = it.point,password = it.password))
                    parentFragmentManager.setFragmentResult("name", bundle)
                    dialog?.dismiss()
                }
                hasFixedSize()
            }
        }
    }

    private suspend fun userList(): List<User> = withContext(Dispatchers.IO) {
        val response = RemoteDataSource.retrofit.getUsers()
        withContext(Dispatchers.Main) {
            response.map {
                User(
                    id = it.id,
                    password = it.password,
                    point = it.point,
                    name = it.name,
                    plan = it.plan
                )
            }
        }
    }

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            GlobalScope.launch(Dispatchers.Main) {
                val fragment = ChooseUserDialogFragment()
                fragment.show(
                    fragmentManager,
                    ChooseUserDialogFragment::class.java.simpleName
                )
            }
        }
    }

}