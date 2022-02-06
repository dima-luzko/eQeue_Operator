package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.databinding.FragmentSettingsBinding
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

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

        setSwitchState()

        binding.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setSwitchState() {
        with(binding) {
            switchPostpone.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    PreferencesManager.getInstance(requireContext())
                        .putBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, true)
                } else {
                    PreferencesManager.getInstance(requireContext())
                        .putBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, false)
                }
            }
            switchRedirect.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    PreferencesManager.getInstance(requireContext())
                        .putBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, true)
                } else {
                    PreferencesManager.getInstance(requireContext())
                        .putBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, false)
                }
            }
            switchOneButtonMode.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    switchRedirect.isEnabled = false
                    switchPostpone.isEnabled = false
                    PreferencesManager.getInstance(requireContext())
                        .putBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, true)
                } else {
                    PreferencesManager.getInstance(requireContext())
                        .putBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)
                }
            }

            switchPostpone.isChecked = PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, false)
            switchRedirect.isChecked = PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, false)
            switchOneButtonMode.isChecked = PreferencesManager.getInstance(requireContext())
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)
        }
    }
}