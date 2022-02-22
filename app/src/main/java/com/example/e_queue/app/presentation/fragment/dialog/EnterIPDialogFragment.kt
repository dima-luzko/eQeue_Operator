package com.example.e_queue.app.presentation.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.e_queue.R
import com.example.e_queue.databinding.FragmentEnterIpDialogBinding
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import kotlinx.android.synthetic.main.fragment_enter_ip_dialog.*

class EnterIPDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentEnterIpDialogBinding
    private var bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterIpDialogBinding.inflate(inflater, container, false)
        dialog?.window?.let {
            with(it) {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        dialog?.setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkChangeText()
        saveIP()
        setSavedIP()
        closeDialog()
    }

    private fun setSavedIP() {
        with(binding) {
            inputIp1.setText(
                PreferencesManager.getInstance(requireContext())
                    .getString(PreferencesManager.PREF_IP_1, "")
            )
            inputIp2.setText(
                PreferencesManager.getInstance(requireContext())
                    .getString(PreferencesManager.PREF_IP_2, "")
            )
            inputIp3.setText(
                PreferencesManager.getInstance(requireContext())
                    .getString(PreferencesManager.PREF_IP_3, "")
            )
            inputIp4.setText(
                PreferencesManager.getInstance(requireContext())
                    .getString(PreferencesManager.PREF_IP_4, "")
            )
            inputIp5.setText(
                PreferencesManager.getInstance(requireContext())
                    .getString(PreferencesManager.PREF_IP_5, "")
            )
        }
    }

    private fun closeDialog() {
        with(binding) {
            close.setOnClickListener {
                errorText.visibility = View.GONE
                dismiss()
            }
        }
    }

    private fun checkChangeText() {
        with(binding) {
            inputIp1.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().toIntOrNull() ?: 0 > 254 || s.toString() == "0") {
                        inputIp1.setText("")
                        inputIp1.setBackgroundResource(R.drawable.ic_vvod_ip_error)
                    } else {
                        inputIp1.setBackgroundResource(R.drawable.ic_vvod_ip)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
            inputIp2.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().toIntOrNull() ?: 0 > 254 || s.toString() == "00") {
                        inputIp2.setText("")
                        inputIp2.setBackgroundResource(R.drawable.ic_vvod_ip_error)
                    } else {
                        inputIp2.setBackgroundResource(R.drawable.ic_vvod_ip)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {}
            })
            inputIp3.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().toIntOrNull() ?: 0 > 254 || s.toString() == "00") {
                        inputIp3.setText("")
                        inputIp3.setBackgroundResource(R.drawable.ic_vvod_ip_error)
                    } else {
                        inputIp3.setBackgroundResource(R.drawable.ic_vvod_ip)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {}
            })
            inputIp4.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().toIntOrNull() ?: 0 > 254 || s.toString() == "00") {
                        inputIp4.setText("")
                        inputIp4.setBackgroundResource(R.drawable.ic_vvod_ip_error)
                    } else {
                        inputIp4.setBackgroundResource(R.drawable.ic_vvod_ip)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {}
            })
            inputIp5.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString() == "00") {
                        inputIp5.setText("")
                        inputIp5.setBackgroundResource(R.drawable.ic_vvod_ip_error)
                    } else {
                        inputIp5.setBackgroundResource(R.drawable.ic_vvod_ip)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }

    }

    private fun saveIP() {
        with(binding) {
            saveIpButton.setOnClickListener {
                if (inputIp1.text.isEmpty() || inputIp2.text.isEmpty() || inputIp3.text.isEmpty() || inputIp4.text.isEmpty() || inputIp5.text.isEmpty()) {
                    errorText.visibility = View.VISIBLE
                } else {
                    errorText.visibility = View.GONE
                    val ip1 = input_ip1.text.toString()
                    val ip2 = input_ip2.text.toString()
                    val ip3 = input_ip3.text.toString()
                    val ip4 = input_ip4.text.toString()
                    val ip5 = input_ip5.text.toString()
                    val glueIP =
                        ip1 + getString(R.string.dot) + ip2 + getString(R.string.dot) + ip3 + getString(
                            R.string.dot
                        ) + ip4 + getString(R.string.colon) + ip5
                    PreferencesManager.getInstance(requireContext())
                        .putString(PreferencesManager.PREF_IP_1, ip1)
                    PreferencesManager.getInstance(requireContext())
                        .putString(PreferencesManager.PREF_IP_2, ip2)
                    PreferencesManager.getInstance(requireContext())
                        .putString(PreferencesManager.PREF_IP_3, ip3)
                    PreferencesManager.getInstance(requireContext())
                        .putString(PreferencesManager.PREF_IP_4, ip4)
                    PreferencesManager.getInstance(requireContext())
                        .putString(PreferencesManager.PREF_IP_5, ip5)
                    PreferencesManager.getInstance(requireContext())
                        .putString(PreferencesManager.PREF_GLUE_IP, glueIP)
                    bundle.putString(Constants.IP_ADDRESS_ARG, glueIP)
                    parentFragmentManager.setFragmentResult(
                        Constants.IP_ADDRESS_REQUEST_KEY,
                        bundle
                    )
                    dialog?.dismiss()
                }
            }
        }
    }

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            val fragment = EnterIPDialogFragment()
            fragment.show(
                fragmentManager,
                EnterIPDialogFragment::class.java.simpleName
            )
        }
    }
}