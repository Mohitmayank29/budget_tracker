package com.example.jetpack1.datastore

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
object PreferencesEncryptedShared {
    const val commonStudentName = "commonStudentName"

    private var securePrefs: EncryptedSharedPreferences? = null

    fun init(context: Context) {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            securePrefs = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ) as EncryptedSharedPreferences?
        }catch (e: Exception) {
            Log.e("SECURE_PREF", "Encryption failed, fallback to normal prefs")

            securePrefs = null
        }
    }

    fun setPreferenceEncryptedShared(key: String,value: String) {
        securePrefs?.edit()?.putString(key, value)?.apply()
    }

    fun getPreferenceEncryptedShared(key: String): String? {
        return securePrefs?.getString(key, null)
    }

}