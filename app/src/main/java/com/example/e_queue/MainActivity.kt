package com.example.e_queue

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.e_queue.app.presentation.fragment.LoginFragment
import com.example.e_queue.app.presentation.fragment.SettingFragment
import com.example.e_queue.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var buttonDown: Long = 0
    private var buttonUp: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var result: Boolean
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                buttonDown = System.currentTimeMillis() / 1000
                result = true
            }
            KeyEvent.KEYCODE_VOLUME_UP -> {
                buttonUp = System.currentTimeMillis() / 1000
                result = true
            }
            else -> result = super.onKeyDown(keyCode, event)
        }
        if (buttonDown - buttonUp in 0..3 || buttonUp - buttonDown in 0..3) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SettingFragment()).addToBackStack(null)
            transaction.commit()
            result = true
        }
        return result
    }

}