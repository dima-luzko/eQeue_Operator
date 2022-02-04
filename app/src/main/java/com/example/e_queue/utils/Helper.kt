package com.example.e_queue.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.e_queue.R
import com.google.android.material.snackbar.Snackbar

fun changeBackgroundAndNavBarColor(fragmentActivity: FragmentActivity, colorResourcesId: Int) {
    with(fragmentActivity.window) {
        navigationBarColor =
            Color.parseColor(fragmentActivity.resources.getString(colorResourcesId))
        setBackgroundDrawable(ContextCompat.getDrawable(fragmentActivity, R.drawable.bg_gradient))
    }
}

fun snackBar(view: View,context: Context, message: Int) {
    val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.snackBar))
    val snackBarView: View = snackBar.view
    val snackBarText: TextView =
        snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
    with(snackBarText) {
        setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.attention_snack_bar,
            0,
            0,
            0
        )
        text = resources.getString(message)
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        gravity = Gravity.CENTER
    }
    snackBar.show()
}
