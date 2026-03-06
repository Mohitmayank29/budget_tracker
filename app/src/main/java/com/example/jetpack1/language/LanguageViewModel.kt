package com.example.jetpack1.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.languagedatastore.LanguageDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
        private val languageDataStore: LanguageDataStore
    ) : ViewModel() {
    val language = languageDataStore.languageFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        "en"
    )

    fun changeLanguage(lang: String) {

        viewModelScope.launch {

            languageDataStore.saveLanguage(lang)

            val locale = LocaleListCompat.forLanguageTags(lang)

            AppCompatDelegate.setApplicationLocales(locale)

        }

    }
        val selectedLanguage = languageDataStore.languageFlow
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                "en"
            )

}
