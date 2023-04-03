package com.rehan.notesappmvvmretrofithilt.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    // Shared preference is used to store small piece of information that persists even if your app is killed. It will be removed once you uninstall the app. Tokens are generally stored inside shared preference.
    // This is how you implement logged in functionality once the user logs in to your app.
    private var preferences =
        context.getSharedPreferences(Constants.PREFERENCES_TOKEN_FILE, Context.MODE_PRIVATE)        // Constants.PREFERENCES_TOKEN_FILE is the file name where we are saving our token

    // Storing token in shared preferences
    fun saveToken(token: String) {
        val editor = preferences.edit()
        editor.putString(Constants.USER_TOKEN, token)   // key : value
        editor.apply()
    }

    fun getToken(): String? {
        return preferences.getString(Constants.USER_TOKEN, null)
    }
}