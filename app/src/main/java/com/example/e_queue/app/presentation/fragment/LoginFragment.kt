package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.SelectedUser
import com.example.e_queue.app.presentation.viewModel.CheckServerViewModel
import com.example.e_queue.app.presentation.viewModel.SelectedUserViewModel
import com.example.e_queue.databinding.FragmentLoginBinding
import com.example.e_queue.utils.Constants.Companion.LOGGED_USER_ARG
import com.example.e_queue.utils.Constants.Companion.SELECTED_USER_ARG
import com.example.e_queue.utils.Constants.Companion.SELECTED_USER_REQUEST_KEY
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import com.example.e_queue.utils.snackBar
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var changeEye = false
    private var bundle = Bundle()
    private val userNameViewModel by viewModel<SelectedUserViewModel>()
    private val serverCheckViewModel by sharedViewModel<CheckServerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        changeBackgroundAndNavBarColor(requireActivity(), R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseUser()
        lookPassword()
        checkLoginAndPassword()
        setSelectedUserInViewModel()
        setSelectedUser()
        goToSettingScreen()

        serverCheckViewModel.statusWorkServer.observe(viewLifecycleOwner){
                with(binding) {
                    if (!it) {
                        inputLogin.alpha = 0.7F
                        inputLogin.isClickable = false
                        buttonMore.alpha = 0.7F
                        inputPassword.alpha = 0.7F
                        inputPassword.isEnabled = false
                        signIn.alpha = 0.7F
                        signIn.isClickable = false
                        eyeIcon.alpha = 0.7F
                        eyeIcon.isClickable = false
                        cardNoConnection.isVisible = true
                    } else {
                        inputLogin.alpha = 1F
                        inputLogin.isClickable = true
                        buttonMore.alpha = 1F
                        inputPassword.alpha = 1F
                        inputPassword.isEnabled = true
                        signIn.alpha = 1F
                        signIn.isClickable = true
                        eyeIcon.alpha = 1F
                        eyeIcon.isClickable = true
                        cardNoConnection.isVisible = false
                    }
                }
            }
    }

    private fun setSelectedUser() {
        userNameViewModel.userName.observe(viewLifecycleOwner) {
            binding.userName.text = it.name
        }
    }

    private fun goToSettingScreen(){
        binding.setting.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SettingFragment()).addToBackStack(null)
            transaction.commit()
        }
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
                    snackBar(requireView(), requireContext(), R.string.choose_user_snack_bar)
                }
            }
            userNameViewModel.userName.observe(viewLifecycleOwner) { user ->
                signIn.setOnClickListener {
                    if (inputPassword.text.toString() == user.password || user.password.isEmpty()) {
                        replaceFragment(
                            fragment = MainFragment(),
                            id = user.id,
                            name = user.name,
                            point = user.point
                        )
                    } else if (user.password.isNotEmpty() && inputPassword.text.toString()
                            .isEmpty()
                    ) {
                        allertImage.visibility = View.VISIBLE
                        inputPassword.setBackgroundResource(R.drawable.error_input)
                        snackBar(requireView(), requireContext(), R.string.enter_password_snack_bar)
                    } else if (inputPassword.text.toString() != user.password) {
                        allertImage.visibility = View.VISIBLE
                        inputPassword.setBackgroundResource(R.drawable.error_input)
                        snackBar(requireView(), requireContext(), R.string.wrong_password_snack_bar)
                    }
                }
            }
        }
    }

    private fun replaceFragment(
        fragment: Fragment,
        id: Int,
        name: String,
        point: String
    ) {
        bundle.putParcelable(
            LOGGED_USER_ARG,
            LoggedUser(
                id = id,
                name = name,
                point = point
            )
        )
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun setSelectedUserInViewModel() {
        parentFragmentManager.setFragmentResultListener(
            SELECTED_USER_REQUEST_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val user = bundle.get(SELECTED_USER_ARG) as SelectedUser
            userNameViewModel.setUserName(user)
        }
    }

}