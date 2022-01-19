package com.example.e_queue.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.e_queue.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getUserInfo()
    }

    private fun getUserInfo(){
        parentFragmentManager.setFragmentResultListener(
            "name", this
        ) { _, bundle ->
            val result = bundle.getString("UserName")
            Toast.makeText(requireContext(),result,Toast.LENGTH_LONG).show()
        }
    }

}