package com.rehan.notesappmvvmretrofithilt.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var preferences =
        context.getSharedPreferences(Constants.PREFERENCES_TOKEN_FILE, Context.MODE_PRIVATE)        // Constants.PREFERENCES_TOKEN_FILE is the file name where we are saving our token

    fun saveToken(token: String) {
        val editor = preferences.edit()
        editor.putString(Constants.USER_TOKEN, token)   // key : value
        editor.apply()
    }

    fun getToken(): String? {
        return preferences.getString(Constants.USER_TOKEN, null)
    }
}