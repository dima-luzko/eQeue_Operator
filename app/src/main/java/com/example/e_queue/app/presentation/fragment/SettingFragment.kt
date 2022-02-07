package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.app.presentation.viewModel.SettingViewModel
import com.example.e_queue.databinding.FragmentSettingsBinding
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingViewModel by viewModel<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
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

        setSwitchStateToViewModelAndSharedPrefs()
        checkSwitchState()
        setEnableSwitch()

        binding.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setSwitchStateToViewModelAndSharedPrefs(){
        with(binding) {
            switchPostpone.setOnCheckedChangeListener { _, isChecked ->
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, isChecked)
                settingViewModel.changeStateSwitchPostponed(isChecked)
            }
            switchRedirect.setOnCheckedChangeListener { _, isChecked ->
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, isChecked)
                settingViewModel.changeStateSwitchRedirect(isChecked)
            }
            switchOneButtonMode.setOnCheckedChangeListener { _, isChecked ->
                PreferencesManager.getInstance(requireContext())
                    .putBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, isChecked)
                settingViewModel.changeStateSwitchOneButtonMode(isChecked)
            }
        }
    }

    private fun checkSwitchState() {
        with(binding) {
            switchPostpone.isChecked = PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, false)
            switchRedirect.isChecked = PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, false)
            switchOneButtonMode.isChecked = PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)
        }
    }

    private fun setEnableSwitch(){
        with(binding){
            settingViewModel.uiState.observe(viewLifecycleOwner){
                switchPostpone.isEnabled = !it.first
                switchRedirect.isEnabled = !it.second
                switchOneButtonMode.isEnabled = !it.third
            }

            switchOneButtonMode.isEnabled = !(PreferencesManager.getInstance(requireContext())
                .getBoolean(
                    PreferencesManager.PREF_SWITCH_POSTPONED,
                    false
                ) || PreferencesManager.getInstance(requireContext())
                .getBoolean(
                    PreferencesManager.PREF_SWITCH_REDIRECT,
                    false
                ))
            switchPostpone.isEnabled = !PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)

            switchRedirect.isEnabled = !PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)
        }
    }
}