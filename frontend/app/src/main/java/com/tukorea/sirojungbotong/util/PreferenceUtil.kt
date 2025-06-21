package com.tukorea.sirojungbotong.util

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtil {
    private const val PREF_NAME = "sirojung_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_NICKNAME = "nickname"
    private const val KEY_ROLE = "role"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveTokens(context: Context, accessToken: String, refreshToken: String) {
        getPrefs(context).edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
        }.commit()
    }

    fun setAccessToken(context: Context, token: String) {
        getPrefs(context).edit().putString(KEY_ACCESS_TOKEN, token).commit()
    }

    fun setRefreshToken(context: Context, token: String) {
        getPrefs(context).edit().putString(KEY_REFRESH_TOKEN, token).commit()
    }

    fun setNickname(context: Context, nickname: String) {
        getPrefs(context).edit().putString(KEY_NICKNAME, nickname).commit()
    }

    fun setRole(context: Context, role: String) {
        getPrefs(context).edit().putString(KEY_ROLE, role).commit()
    }

    fun getAccessToken(context: Context): String? {
        return getPrefs(context).getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRole(context: Context): String? {
        return getPrefs(context).getString(KEY_ROLE, null)
    }

    fun clearTokens(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}