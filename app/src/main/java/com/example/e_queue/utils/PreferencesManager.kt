package com.example.e_queue.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        const val PREF_SELECTED_RESULTS = "PREF_SELECTED_RESULTS"
        const val PREF_BUTTON_LOOK_POSTPONED_LIST_CUSTOMER = "prefButtonLookPostponedListCustomer"
        const val PREF_BUTTON_START_WORK_WITH_CUSTOMER = "prefButtonStartWorkWithCustomer"
        const val PREF_BUTTON_INVITE_AGAIN_CUSTOMER = "prefButtonInviteAgainCustomer"

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
