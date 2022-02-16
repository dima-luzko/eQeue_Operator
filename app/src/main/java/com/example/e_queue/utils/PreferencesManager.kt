package com.example.e_queue.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        const val PREF_SWITCH_POSTPONED = "PREF_SWITCH_POSTPONED"
        const val PREF_SWITCH_REDIRECT = "PREF_SWITCH_REDIRECT"
        const val PREF_SWITCH_ONE_MODE = "PREF_SWITCH_ONE_MODE"
        const val PREF_FLAG = "PREF_FLAG"
        const val PREF_ON_BACK_PRESSED = "PREF_ON_BACK_PRESSED"

        private var ourInstance: PreferencesManager? = null
        fun getInstance(context: Context): PreferencesManager {
            if (ourInstance == null) {
                ourInstance = PreferencesManager(context)
            }
            return ourInstance!!
        }
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }


}
