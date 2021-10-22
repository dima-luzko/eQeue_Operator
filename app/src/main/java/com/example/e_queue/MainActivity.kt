package com.example.e_queue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.e_queue.databinding.ActivityMainBinding
import com.example.e_queue.databinding.ClientRedirectionBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ClientRedirectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ClientRedirectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}