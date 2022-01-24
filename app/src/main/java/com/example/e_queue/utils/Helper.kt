package com.example.e_queue.utils

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.e_queue.R

fun changeBackgroundAndNavBarColor(fragmentActivity: FragmentActivity, colorResourcesId: Int) {
    with(fragmentActivity.window) {
        navigationBarColor =
            Color.parseColor(fragmentActivity.resources.getString(colorResourcesId))
        setBackgroundDrawable(ContextCompat.getDrawable(fragmentActivity, R.drawable.bg_gradient))
    }
}
