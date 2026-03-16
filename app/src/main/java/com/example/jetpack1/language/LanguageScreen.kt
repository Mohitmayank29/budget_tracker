package com.example.jetpack1.language

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.R
import com.example.jetpack1.common.CommonButton
import com.example.jetpack1.common.CommonTopbar
import com.example.jetpack1.localmanager.LanguagePreference
import com.example.jetpack1.localmanager.LocaleManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController, viewModel: LanguageViewModel = hiltViewModel()) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val languages = listOf(
        Language("en", stringResource(R.string.english)),
        Language("hi", stringResource(R.string.hindi)),
        Language("mr", stringResource(R.string.marathi))
    )
    var isClickable by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var selectedLang by remember { mutableStateOf(selectedLanguage) }
    LaunchedEffect(selectedLanguage) {
        selectedLang = selectedLanguage
    }
    val context = LocalContext.current
    val activity = (context as? Activity)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        CommonTopbar(
            title = "Change Language",
            onbackclick = { navController.popBackStack() },
        )
        Column(
            Modifier.fillMaxSize().padding(10.dp)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(languages) { language ->
                   Log.d("language",language.code)
                    LanguageItem(
                        language = language,
                        selected = selectedLang == language.code,
                        onSelect = {
                            selectedLang = language.code
                            Log.d("selectedLang",selectedLang)
                        }
                    )
                }
            }

            CommonButton(
                text = stringResource(R.string.change_language),
                onClick = {
                    if (isClickable) {
                        isClickable = false
                        Log.d("LANG_CHANGE", "Saving language: $selectedLang")
                        Log.d("LANGUAGE_CLICK", "English Selected")

                        LanguagePreference.saveLanguage(context, selectedLang)
                        LocaleManager.setLocale(context, selectedLang)
                        activity?.recreate()
//                    scope.launch {
//                        viewModel.changeLanguage(selectedLang)
//                        navController.popBackStack()
//                    }

                    }
                }
            )
        }
    }
}

@Composable
fun LanguageItem(
    language: Language,
    selected: Boolean,
    onSelect: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(Modifier.fillMaxWidth().padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = language.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.TopStart)
            )

            RadioButton(
                selected = selected,
                onClick = onSelect,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}


@Preview
@Composable
private fun PreviewLanguageScreen() {
//    val  viewModel: LanguageViewModel = LanguageViewModel()
    LanguageScreen(navController = NavController(LocalContext.current))
}