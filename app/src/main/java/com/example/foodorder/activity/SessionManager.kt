package com.example.foodorder.activity

import android.content.Context
import android.content.SharedPreferences
import com.example.foodorder.model.User
import com.google.gson.Gson

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        val json = Gson().toJson(user)
        prefs.edit().putString("user_data", json).apply()
    }

    fun getUser(): User? {
        val json = prefs.getString("user_data", null)
        return if (json != null) Gson().fromJson(json, User::class.java) else null
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
