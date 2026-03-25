package com.example.jetpack1.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
object PreferencesEncryptedShared {
    const val commonStudentName = "commonStudentName"

    private var securePrefs: EncryptedSharedPreferences? = null

    fun init(context: Context) {
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
    }

    fun setPreferenceEncryptedShared(key: String,value: String) {
        securePrefs?.edit()?.putString(key, value)?.apply()
    }

    fun getPreferenceEncryptedShared(key: String): String? {
        return securePrefs?.getString(key, null)
    }

}