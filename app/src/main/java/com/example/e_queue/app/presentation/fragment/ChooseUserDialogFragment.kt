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
import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.presentation.adapter.ChooseUserItemAdapter
import com.example.e_queue.databinding.FragmentChooseUserDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

    private fun setUserList(){
        with(binding.chooseUserList){
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = ChooseUserItemAdapter(userList) {
                bundle.putString("UserName",it.name)
                parentFragmentManager.setFragmentResult("name",bundle)
                dialog?.dismiss()
            }
            hasFixedSize()
        }
    }

    private val userList = listOf(
        User(
            name = "lox1"
        ),
        User(
            name = "lox2"
        ),
        User(
            name = "lox3"
        ),
        User(
            name = "lox4"
        ),
        User(
            name = "lox5"
        ),
        User(
            name = "lox6"
        )
    )

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