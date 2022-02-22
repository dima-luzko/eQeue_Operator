package com.example.e_queue.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.e_queue.R
import com.example.e_queue.app.presentation.fragment.dialog.EnterIPDialogFragment
import com.example.e_queue.app.presentation.viewModel.SettingViewModel
import com.example.e_queue.databinding.FragmentSettingsBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import com.example.e_queue.utils.changeBackgroundAndNavBarColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingViewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeBackgroundAndNavBarColor(this, R.color.gray_background)
        this.window.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.bg_gradient
            )
        )
        setSwitchStateToViewModelAndSharedPrefs()
        checkSwitchState()
        setEnableSwitch()
        handleClick()
        getSavedIPAddress()
        setSavedIPAddress()
    }

    private fun handleClick() {
        with(binding) {
            icBack.setOnClickListener {
                val intent = Intent(this@SettingActivity, MainActivity::class.java)
                startActivity(intent)
            }
            changeIpAddress.setOnClickListener {
                EnterIPDialogFragment.showDialog(supportFragmentManager)
            }
        }
    }

    private fun setSwitchStateToViewModelAndSharedPrefs() {
        with(binding) {
            switchPostpone.setOnCheckedChangeListener { _, isChecked ->
                PreferencesManager.getInstance(this@SettingActivity)
                    .putBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, isChecked)
                settingViewModel.changeStateSwitchPostponed(isChecked)
            }
            switchRedirect.setOnCheckedChangeListener { _, isChecked ->
                PreferencesManager.getInstance(this@SettingActivity)
                    .putBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, isChecked)
                settingViewModel.changeStateSwitchRedirect(isChecked)
            }
            switchOneButtonMode.setOnCheckedChangeListener { _, isChecked ->
                PreferencesManager.getInstance(this@SettingActivity)
                    .putBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, isChecked)
                settingViewModel.changeStateSwitchOneButtonMode(isChecked)
            }
        }
    }

    private fun checkSwitchState() {
        with(binding) {
            switchPostpone.isChecked = PreferencesManager.getInstance(this@SettingActivity)
                .getBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, false)
            switchRedirect.isChecked = PreferencesManager.getInstance(this@SettingActivity)
                .getBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, false)
            switchOneButtonMode.isChecked = PreferencesManager.getInstance(this@SettingActivity)
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)
        }
    }

    private fun setEnableSwitch() {
        with(binding) {
            settingViewModel.uiState.observe(this@SettingActivity) {
                switchPostpone.isEnabled = !it.first
                switchRedirect.isEnabled = !it.second
                switchOneButtonMode.isEnabled = !it.third
            }

            switchOneButtonMode.isEnabled = !(PreferencesManager.getInstance(this@SettingActivity)
                .getBoolean(
                    PreferencesManager.PREF_SWITCH_POSTPONED,
                    false
                ) || PreferencesManager.getInstance(this@SettingActivity)
                .getBoolean(
                    PreferencesManager.PREF_SWITCH_REDIRECT,
                    false
                ))
            switchPostpone.isEnabled = !PreferencesManager.getInstance(this@SettingActivity)
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)

            switchRedirect.isEnabled = !PreferencesManager.getInstance(this@SettingActivity)
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)
        }
    }

    private fun getSavedIPAddress() {
        supportFragmentManager.setFragmentResultListener(
            Constants.IP_ADDRESS_REQUEST_KEY, this@SettingActivity
        ) { _, bundle ->
            val ip = bundle.getString(Constants.IP_ADDRESS_ARG)
            ip?.let { settingViewModel.setIPAddress(it) }
        }
    }

    private fun setSavedIPAddress() {
        settingViewModel.setIPAddress(
            PreferencesManager.getInstance(this@SettingActivity)
                .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
        )
        settingViewModel.ipAddress.observe(this@SettingActivity) {
            binding.ipAddress.text = it
        }
    }

    override fun onBackPressed() {}
}