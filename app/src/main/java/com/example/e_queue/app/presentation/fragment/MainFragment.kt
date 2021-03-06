package com.example.e_queue.app.presentation.fragment

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.data.model.BodyForFinishWorkWithCustomer
import com.example.e_queue.app.data.model.InvitePostponedClient
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.presentation.viewModel.LoggedUserViewModel
import com.example.e_queue.databinding.FragmentMainBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.Constants.Companion.LOGGED_USER_ARG
import com.example.e_queue.utils.Constants.Companion.OPERATION_WITH_LOGGED_USER_ARG
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val loggedUserViewModel: LoggedUserViewModel by viewModel {
        parametersOf(arguments?.getParcelable<LoggedUser>(LOGGED_USER_ARG))
    }
    private val bundle = Bundle()
    private var statusClient = 0
    private var mMediaPlayer: MediaPlayer? = null

    companion object {
        fun newInstance(loggedUserModel: LoggedUser) = MainFragment().apply {
            arguments = bundleOf(LOGGED_USER_ARG to loggedUserModel)
        }
    }

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
        changeVisibilityOneButtonMode()
        setToolbarText()
        setServiceLength()
        setNextCustomer()
        handleClicks()
        unLoggedUser()
        playSound()
    }

    private fun setToolbarText() {
        loggedUserViewModel.setUserParams()
        loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) {
            with(binding.toolbar) {
                operatorName.text = it.name
                workspaceNumber.text = it.point
            }
        }
    }

    private fun unLoggedUser() {
        with(binding.toolbar.exit) {
            setOnClickListener {
                if (statusClient == 0 || PreferencesManager.getInstance(requireContext())
                        .getBoolean(
                            PreferencesManager.PREF_SWITCH_ONE_MODE,
                            false
                        ) && statusClient == 1
                ) {
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, LoginFragment())
                    transaction.commit()
                }
            }

        }
    }

    private fun playSound() {
        loggedUserViewModel.isPlay.observe(viewLifecycleOwner) {
            mMediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound_for_invite_customer)
            mMediaPlayer!!.start()
            if ( PreferencesManager.getInstance(requireContext())
                    .getBoolean(PreferencesManager.PREF_DONT_PLAY_SOUND, false)
            ) {
                with(binding){
                    include.currentClientNumber.text = ""
                    include.currentClientService.text = ""
                }
                if (mMediaPlayer != null) {
                    mMediaPlayer!!.release()
                    mMediaPlayer = null
                }
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_DONT_PLAY_SOUND, false)

            }
        }
    }

    private fun setServiceLength() {
        loggedUserViewModel.serviceLength.observe(viewLifecycleOwner) { serviceLength ->
            binding.quantity.amountOfClients.text = serviceLength.toString()
        }
    }

    private fun setNextCustomer() {
        loggedUserViewModel.nextCustomerInfo.observe(viewLifecycleOwner) {
            binding.nextClientNumber.text = (it.prefix + it.number)
            binding.nextClientArrow.visibility =
                if (binding.nextClientNumber.text.isEmpty()) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun inviteNextCustomer() {
        loggedUserViewModel.inviteNextCustomer()
    }

    private fun killNextCustomer() {
        loggedUserViewModel.killNextCustomer()
        with(binding.include) {
            currentClientNumber.text = ""
            currentClientService.text = ""
        }
    }

    private fun getStartWorkWithCustomer() {
        loggedUserViewModel.getStartCustomer()
    }

    private fun changeVisibilityOneButtonMode() {
        with(binding.someButton) {
            if (PreferencesManager.getInstance(requireContext())
                    .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)
            ) {
                buttonCallNextClientOneMode.isVisible = true
                buttonCallNextClientAgainOneMode.isVisible = true
                buttonCallNextClient.isVisible = false
                buttonListPostponedClients.isVisible = false
            } else {
                buttonCallNextClientOneMode.isVisible = false
                buttonCallNextClientAgainOneMode.isVisible = false
                buttonCallNextClient.isVisible = true
                buttonListPostponedClients.isVisible = true
            }
        }
    }

    private fun handleClicks() {
        changeVisibilityOneButtonMode()
        loggedUserViewModel.serviceLength.observe(viewLifecycleOwner) {
            with(binding) {
                if (it == 0) {
                    someButton.buttonCallNextClient.isEnabled = false
                    someButton.buttonCallNextClientOneMode.isEnabled = false
                    someButton.buttonCallNextClient.background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.disable_button)
                    someButton.buttonCallNextClientOneMode.background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.disable_button)
                    someButton.buttonCallNextClient.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_text
                        )
                    )
                    someButton.buttonCallNextClientOneMode.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_text
                        )
                    )
                } else {
                    someButton.buttonCallNextClient.isEnabled = true
                    someButton.buttonCallNextClientOneMode.isEnabled = true
                    someButton.buttonCallNextClient.background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.green_button)
                    someButton.buttonCallNextClientOneMode.background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.green_button)
                    someButton.buttonCallNextClient.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    someButton.buttonCallNextClientOneMode.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
                if (it == 0 && include.currentClientNumber.text.isEmpty() || include.currentClientNumber.text.isEmpty() && include.currentClientService.text.isEmpty()) {
                    with(someButton.buttonCallNextClientAgainOneMode) {
                        isEnabled = false
                        background =
                            ContextCompat.getDrawable(requireActivity(), R.drawable.disable_button)
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_text
                            )
                        )
                    }
                } else {
                    with(someButton.buttonCallNextClientAgainOneMode) {
                        isEnabled = true
                        background =
                            ContextCompat.getDrawable(requireActivity(), R.drawable.green_button)
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                }
            }
        }
        with(binding.someButton) {
            buttonCallNextClientOneMode.setOnClickListener {
                statusClient = 0
                killNextCustomer()
                changeStatusClient()

                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    statusClient = 1
                    inviteNextCustomer()
                    setInviteCustomerInfo()
                    changeStatusClient()
                }
            }
            buttonCallNextClientAgainOneMode.setOnClickListener {
                statusClient = 1
                inviteNextCustomer()
                setInviteCustomerInfo()
                changeStatusClient()
            }
            buttonCallNextClient.setOnClickListener {
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_REDIRECT_CUSTOMER, false)
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_POSTPONED_CUSTOMER, false)
                statusClient = 1
                inviteNextCustomer()
                with(binding.someButton) {
                    buttonCallNextClient.isVisible = false
                    buttonListPostponedClients.isVisible = false
                    buttonStartWork.isVisible = true
                    buttonCallNextClientAgain.isVisible = true
                    buttonNoClient.isVisible = true
                }
                changeStatusClient()
            }
            buttonListPostponedClients.setOnClickListener {
                loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                    replaceFragment(
                        fragment = PostponedClientListFragment(),
                        userId = loggedUser.id,
                        userName = loggedUser.name,
                        point = loggedUser.point,
                        clientNumber = binding.include.currentClientNumber.text.toString()
                    )
                }
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_FLAG, false)
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_REDIRECT_CUSTOMER, false)
            }
            buttonStartWork.setOnClickListener {
                statusClient = 2
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_ON_BACK_PRESSED, false)
                with(binding.someButton) {
                    buttonStartWork.isVisible = false
                    buttonCallNextClientAgain.isVisible = false
                    buttonNoClient.isVisible = false
                    buttonPostpone.isVisible = PreferencesManager.getInstance(requireContext())
                        .getBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, false)
                    buttonRedirect.isVisible = PreferencesManager.getInstance(requireContext())
                        .getBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, false)
                    buttonFinishWork.isVisible = true
                }
                getStartWorkWithCustomer()
                changeStatusClient()
            }
            buttonCallNextClientAgain.setOnClickListener {
                statusClient = 1
                inviteNextCustomer()
                changeStatusClient()
            }
            buttonNoClient.setOnClickListener {
                statusClient = 0
                killNextCustomer()
                with(binding.someButton) {
                    buttonStartWork.isVisible = false
                    buttonCallNextClientAgain.isVisible = false
                    buttonNoClient.isVisible = false
                    buttonCallNextClient.isVisible = true
                    buttonListPostponedClients.isVisible = true
                }
                changeStatusClient()
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_FLAG, false)
            }

            buttonFinishWork.setOnClickListener {
                if (PreferencesManager.getInstance(requireContext())
                        .getBoolean(PreferencesManager.PREF_FLAG, false)
                ) {
                    val res =
                        arguments?.getParcelable<InvitePostponedClient>(Constants.INVITE_POSTPONED_ARG)
                    loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                        if (res?.resultRequired == false) {
                            val body = BodyForFinishWorkWithCustomer(
                                userId = loggedUser.id,
                                resultId = -1
                            )
                            finishWorkWithCustomer(body)
                        } else {
                            replaceFragment(
                                fragment = ResultFragment(),
                                userId = loggedUser.id,
                                userName = loggedUser.name,
                                point = loggedUser.point,
                                clientNumber = binding.include.currentClientNumber.text.toString()
                            )
                        }
                    }
                } else {
                    loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                        if (binding.toolbar.invisibleResultRequired.text == "false") {
                            val body = BodyForFinishWorkWithCustomer(
                                userId = loggedUser.id,
                                resultId = -1
                            )
                            finishWorkWithCustomer(body)
                        } else {
                            replaceFragment(
                                fragment = ResultFragment(),
                                userId = loggedUser.id,
                                userName = loggedUser.name,
                                point = loggedUser.point,
                                clientNumber = binding.include.currentClientNumber.text.toString()
                            )
                        }
                    }
                }
            }

            buttonRedirect.setOnClickListener {
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_REDIRECT_CUSTOMER, false)
                loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                    replaceFragment(
                        fragment = RedirectClientFragment(),
                        userId = loggedUser.id,
                        userName = loggedUser.name,
                        point = loggedUser.point,
                        clientNumber = binding.include.currentClientNumber.text.toString()
                    )
                }
            }

            buttonPostpone.setOnClickListener {
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_POSTPONED_CUSTOMER, false)
                loggedUserViewModel.loggedUser.observe(viewLifecycleOwner) { loggedUser ->
                    replaceFragment(
                        fragment = PostponedClientFragment(),
                        userId = loggedUser.id,
                        userName = loggedUser.name,
                        point = loggedUser.point,
                        clientNumber = binding.include.currentClientNumber.text.toString()
                    )
                }
            }
        }
    }

    private fun finishWorkWithCustomer(body: BodyForFinishWorkWithCustomer) {
        statusClient = 0
        with(binding) {
            someButton.buttonCallNextClient.isVisible = true
            someButton.buttonListPostponedClients.isVisible = true
            someButton.buttonPostpone.isVisible = false
            someButton.buttonRedirect.isVisible = false
            someButton.buttonFinishWork.isVisible = false
            include.currentClientNumber.text = ""
            include.currentClientService.text = ""
        }
        changeStatusClient()
        loggedUserViewModel.finishWorkWithCustomer(body)
        PreferencesManager.getInstance(requireContext())
            .putBoolean(PreferencesManager.PREF_FLAG, false)
        PreferencesManager.getInstance(requireContext())
            .putBoolean(PreferencesManager.PREF_ON_BACK_PRESSED, false)
    }

    private fun replaceFragment(
        fragment: Fragment,
        userId: Int,
        userName: String,
        point: String,
        clientNumber: String?
    ) {
        bundle.putParcelable(
            OPERATION_WITH_LOGGED_USER_ARG,
            OperationWithLoggedUser(
                userId = userId,
                userName = userName,
                point = point,
                clientNumber = clientNumber
            )
        )
        fragment.arguments = bundle
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment).addToBackStack(null)
        transaction.commit()
    }

    private fun changeStatusClient() {
        with(binding.statusOfClient) {
            when (statusClient) {
                0 -> {
                    background = ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.status_client_is_not_called
                    )
                    text = getString(R.string.status_client_is_not_called)
                }
                1 -> {
                    background = ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.status_client_is_called
                    )
                    text = getString(R.string.status_client_is_called)
                }
                2 -> {
                    background = ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.status_work_with_client
                    )
                    text = getString(R.string.status_work_with_client)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setInviteCustomerInfo() {
        loggedUserViewModel.inviteNextCustomerInfo.observe(viewLifecycleOwner) {
            with(binding) {
                include.currentClientNumber.text = it.prefix + it.number
                include.currentClientService.text = it.serviceName.name
                toolbar.invisibleResultRequired.text = it.serviceName.resultRequired.toString()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setInvitePostponedCustomerInfo() {
        if (PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_FLAG, false)
        ) {
            val res =
                arguments?.getParcelable<InvitePostponedClient>(Constants.INVITE_POSTPONED_ARG)
            with(binding) {
                include.currentClientNumber.text = res?.prefix + res?.number
                include.currentClientService.text = res?.serviceName
                with(someButton) {
                    buttonCallNextClient.isVisible = false
                    buttonListPostponedClients.isVisible = false
                    buttonStartWork.isVisible = true
                    buttonCallNextClientAgain.isVisible = true
                    buttonNoClient.isVisible = true
                    buttonPostpone.isVisible = false
                    buttonRedirect.isVisible = false
                    buttonFinishWork.isVisible = false
                }
            }
            statusClient = 1
            changeStatusClient()
        }
    }

    private fun checkBackPressed() {
        if (PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_ON_BACK_PRESSED, false)
        ) {
            with(binding.someButton) {
                if (!PreferencesManager.getInstance(requireContext())
                        .getBoolean(PreferencesManager.PREF_POSTPONED_CUSTOMER, false)
                    || !PreferencesManager.getInstance(requireContext())
                        .getBoolean(PreferencesManager.PREF_REDIRECT_CUSTOMER, false)
                ) {
                    buttonStartWork.isVisible = false
                    buttonCallNextClientAgain.isVisible = false
                    buttonNoClient.isVisible = false
                }
                buttonPostpone.isVisible = PreferencesManager.getInstance(requireContext())
                    .getBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, false)
                buttonRedirect.isVisible = PreferencesManager.getInstance(requireContext())
                    .getBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, false)
                buttonFinishWork.isVisible = true
            }
        }
    }

    private fun checkPostponedCustomer() {
        if (PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_POSTPONED_CUSTOMER, false)
            || PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_REDIRECT_CUSTOMER, false)
        ) {
            statusClient = 0
            changeStatusClient()
            with(binding) {
                with(someButton) {
                    buttonStartWork.isVisible = false
                    buttonCallNextClientAgain.isVisible = false
                    buttonNoClient.isVisible = false
                    buttonCallNextClient.isVisible = true
                    buttonListPostponedClients.isVisible = true
                    buttonPostpone.isVisible = false
                    buttonRedirect.isVisible = false
                    buttonFinishWork.isVisible = false
                }
                include.currentClientNumber.text = ""
                include.currentClientService.text = ""
            }
        }
    }


    override fun onResume() {
        super.onResume()
        checkBackPressed()
        setInviteCustomerInfo()
        setInvitePostponedCustomerInfo()
        changeStatusClient()
        checkPostponedCustomer()
    }

    override fun onStart() {
        super.onStart()
        with(loggedUserViewModel) {
            startGetServiceLength()
            startGetNextCustomerInfo()
        }
    }

    override fun onStop() {
        super.onStop()
        with(loggedUserViewModel) {
            stopGetServiceLength()
            stopGetNextCustomerInfo()
        }
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

}