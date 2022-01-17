package com.example.e_queue.app.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var changeEye = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with( binding.inputPassword) {

        }


        chooseUser()
        lookPassword()
        getUserInfo()
    }



    private fun chooseUser(){
        binding.buttonMore.setOnClickListener {
            ChooseUserDialogFragment.showDialog(parentFragmentManager)
        }
    }

    private fun getUserInfo(){
        parentFragmentManager.setFragmentResultListener(
            "name", this
        ) { _, bundle ->
            val result = bundle.getString("UserName")
            binding.userName.text = result
        }
    }

    private fun lookPassword() {
        binding.eyeIcon.setOnClickListener {
            changeEye = if (changeEye) {
                binding.eyeIcon.setImageResource(R.drawable.ic_close_eye)
                binding.inputPassword.transformationMethod = null
                false
            } else {
                binding.eyeIcon.setImageResource(R.drawable.ic_open_eye)
                binding.inputPassword.transformationMethod = PasswordTransformationMethod()
                true
            }
        }

    }


}