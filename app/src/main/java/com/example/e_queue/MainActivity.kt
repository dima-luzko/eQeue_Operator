package com.example.e_queue

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.e_queue.app.presentation.fragment.LoginFragment
import com.example.e_queue.app.presentation.viewModel.CheckServerViewModel
import com.example.e_queue.databinding.ActivityMainBinding
import com.example.e_queue.utils.PreferencesManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val serverCheckViewModel by viewModel<CheckServerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeSharedPrefs()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, LoginFragment())
        transaction.commit()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun changeSharedPrefs(){
        PreferencesManager.getInstance(this)
            .putBoolean(PreferencesManager.PREF_FLAG, false)
        PreferencesManager.getInstance(this)
            .putBoolean(PreferencesManager.PREF_REDIRECT_CUSTOMER, false)
        PreferencesManager.getInstance(this)
            .putBoolean(PreferencesManager.PREF_POSTPONED_CUSTOMER, false)
        PreferencesManager.getInstance(this)
            .putBoolean(PreferencesManager.PREF_ON_BACK_PRESSED, false)
    }

    override fun onResume() {
        super.onResume()
        serverCheckViewModel.statusWorkServer.observe(this) {
            if (!it) {
                val transaction2 = supportFragmentManager.beginTransaction()
                transaction2.replace(R.id.fragment_container, LoginFragment())
                transaction2.commit()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        serverCheckViewModel.startCheckServerState()
    }

    override fun onStop() {
        super.onStop()
        serverCheckViewModel.stopCheckServerState()
    }

    override fun onBackPressed() {}
}