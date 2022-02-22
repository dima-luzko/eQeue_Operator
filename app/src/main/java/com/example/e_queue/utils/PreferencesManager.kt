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
        const val PREF_POSTPONED_CUSTOMER = "PREF_POSTPONED_CUSTOMER"
        const val PREF_REDIRECT_CUSTOMER = "PREF_REDIRECT_CUSTOMER"
        const val PREF_ON_BACK_PRESSED = "PREF_ON_BACK_PRESSED"
        const val PREF_IP_1 = "PREF_IP_1"
        const val PREF_IP_2 = "PREF_IP_2"
        const val PREF_IP_3 = "PREF_IP_3"
        const val PREF_IP_4 = "PREF_IP_4"
        const val PREF_IP_5 = "PREF_IP_5"
        const val PREF_GLUE_IP = "PREF_GLUE_IP"

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

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
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

    fun getString(key: String, defValue: String): String {
        return preferences.getString(key, defValue).toString()
    }


}
