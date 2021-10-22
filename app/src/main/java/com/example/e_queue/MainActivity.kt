package com.example.e_queue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.e_queue.databinding.ActivityMainBinding
import com.example.e_queue.databinding.CheckIpConnectionDialogBinding
import com.example.e_queue.databinding.ClientRedirectionBinding
import com.example.e_queue.databinding.SignInBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: CheckIpConnectionDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CheckIpConnectionDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}