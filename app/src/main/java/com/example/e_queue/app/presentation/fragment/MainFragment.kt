package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_queue.R
import com.example.e_queue.databinding.FragmentMainBinding
import com.example.e_queue.utils.changeBackgroundAndNavBarColor


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        changeBackgroundAndNavBarColor(requireActivity(),R.color.gray_background)
        requireActivity().window.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.bg_gradient))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getUserInfo() {
        parentFragmentManager.setFragmentResultListener(
            "name", this
        ) { _, bundle ->
            val result = bundle.getString("UserName")
            Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
        }
    }

}