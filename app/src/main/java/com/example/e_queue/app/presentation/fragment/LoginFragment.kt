package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*


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
        chooseUser()
        lookPassword()
        //setUserName()
        checkLoginAndPassword()
    }


    private fun chooseUser() {
        binding.inputLogin.setOnClickListener {
            ChooseUserDialogFragment.showDialog(parentFragmentManager)
        }
    }

    private fun setUserName() {
        parentFragmentManager.setFragmentResultListener(
            "name", this
        ) { _, bundle ->
            val loggedUser = bundle.get("data") as LoggedUser
            binding.userName.text = loggedUser.name
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

    private fun snackBar(message: Int) {
        val snackBar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.snackBar))
        val snackBarView: View = snackBar.view
        val snackBarText: TextView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
        with(snackBarText) {
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.attention_snack_bar,
                0,
                0,
                0
            )
            text = getString(message)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            gravity = Gravity.CENTER
        }
        snackBar.show()
    }

    private fun checkLoginAndPassword() {
        with(binding) {
            signIn.setOnClickListener {
                if (binding.userName.text == getString(R.string.choose_user)) {
                    snackBar(R.string.choose_user_snack_bar)
                }
            }

            parentFragmentManager.setFragmentResultListener(
                "name", viewLifecycleOwner
            ) { _, bundle ->
                val user = bundle.get("data") as LoggedUser
                userName.text = user.name
                signIn.setOnClickListener {
                    if (inputPassword.text.toString() == user.password || user.password.isEmpty()) {
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, MainFragment())
                        transaction.commit()
                    } else if (user.password.isNotEmpty() && inputPassword.text.toString()
                            .isEmpty()
                    ) {
                        allertImage.visibility = View.VISIBLE
                        inputPassword.setBackgroundResource(R.drawable.error_input)
                        snackBar(R.string.enter_password_snack_bar)
                        GlobalScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                delay(2900)
                            }
                                allertImage.visibility = View.INVISIBLE
                                inputPassword.setBackgroundResource(R.drawable.input_img)
                        }
                    } else if (inputPassword.text.toString() != user.password) {
                        allertImage.visibility = View.VISIBLE
                        inputPassword.setBackgroundResource(R.drawable.error_input)
                        snackBar(R.string.wrong_password_snack_bar)
                        GlobalScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                delay(2900)
                            }
                                allertImage.visibility = View.INVISIBLE
                                inputPassword.setBackgroundResource(R.drawable.input_img)
                        }
                    }
                }
            }
        }


    }
}