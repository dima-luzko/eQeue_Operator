package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.databinding.FragmentLoginBinding
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var changeEye = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        changeBackgroundAndNavBarColor(requireActivity(),R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseUser()
        lookPassword()
        checkLoginAndPassword()
    }


    private fun chooseUser() {
        binding.inputLogin.setOnClickListener {
            ChooseUserDialogFragment.showDialog(parentFragmentManager)
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

    private fun operationWithInputPassword() {
        with(binding) {
            inputPassword.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    allertImage.visibility = View.INVISIBLE
                    inputPassword.setBackgroundResource(R.drawable.input_img)
                }
            }

            inputPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    inputPassword.clearFocus()
                }
                false
            }
        }
    }

    private fun checkLoginAndPassword() {
        with(binding) {
            operationWithInputPassword()
            signIn.setOnClickListener {
                if (userName.text == getString(R.string.choose_user)) {
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
                    } else if (inputPassword.text.toString() != user.password) {
                        allertImage.visibility = View.VISIBLE
                        inputPassword.setBackgroundResource(R.drawable.error_input)
                        snackBar(R.string.wrong_password_snack_bar)
                    }
                }
            }
        }
    }


}