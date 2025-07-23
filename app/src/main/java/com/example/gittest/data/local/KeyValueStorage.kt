package com.example.gittest.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class KeyValueStorage @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        const val PREFS_NAME = "auth_prefs"
        private const val AUTH_TOKEN_KEY = "auth_token"
    }

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN_KEY, null)
        set(value) = prefs.edit { putString(AUTH_TOKEN_KEY, value) }
}