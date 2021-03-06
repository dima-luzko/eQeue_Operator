package com.example.e_queue.app.presentation.fragment.dialog

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
import com.example.e_queue.app.data.model.SelectedUser
import com.example.e_queue.app.presentation.adapter.ChooseUserItemAdapter
import com.example.e_queue.app.presentation.viewModel.UserListViewModel
import com.example.e_queue.databinding.FragmentChooseUserDialogBinding
import com.example.e_queue.utils.Constants.Companion.SELECTED_USER_ARG
import com.example.e_queue.utils.Constants.Companion.SELECTED_USER_REQUEST_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChooseUserDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentChooseUserDialogBinding
    private var bundle = Bundle()
    private val userViewModel by viewModel<UserListViewModel>()

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
        getUserList()
    }

    private fun getUserList() {
        with(userViewModel) {
            getUserList()
            user.observe(viewLifecycleOwner) { observeUserList ->
                with(binding.chooseUserList) {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = ChooseUserItemAdapter(
                        observeUserList.filterIndexed { index, _ -> index != 0 }
                    ) { userAdapter ->
                        bundle.putParcelable(
                            SELECTED_USER_ARG, SelectedUser(
                                id = userAdapter.id,
                                name = userAdapter.name,
                                point = userAdapter.point,
                                password = userAdapter.password
                            )
                        )
                        parentFragmentManager.setFragmentResult(SELECTED_USER_REQUEST_KEY, bundle)
                        dialog?.dismiss()
                    }
                    hasFixedSize()
                }
            }

        }
    }

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            val fragment = ChooseUserDialogFragment()
            fragment.show(
                fragmentManager,
                ChooseUserDialogFragment::class.java.simpleName
            )
        }
    }

}