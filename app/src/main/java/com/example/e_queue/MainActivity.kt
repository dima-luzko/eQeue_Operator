package com.example.e_queue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_queue.app.presentation.fragment.LoginFragment
import com.example.e_queue.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, LoginFragment())
        transaction.commit()

    }

}