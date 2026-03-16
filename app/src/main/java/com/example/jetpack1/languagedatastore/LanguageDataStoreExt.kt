package com.example.jetpack1.languagedatastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.languageDataStore by preferencesDataStore(
    name = "language_pref"
)