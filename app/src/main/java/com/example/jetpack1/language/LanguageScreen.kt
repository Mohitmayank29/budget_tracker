package com.example.jetpack1.language

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.common.CommonButton
import com.example.jetpack1.language.LanguageViewModel


@Composable
fun LanguageScreen(nacontroller: NavController, viewModel: LanguageViewModel = hiltViewModel()) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val languages = listOf(
        Language("en", "English"),
        Language("hi", "Hindi"),
        Language("mr", "Marathi")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(languages) { language ->

                LanguageItem(
                    language = language,
                    selected = selectedLanguage == language.code,
                    onSelect = {
                        viewModel.changeLanguage(language.code)
                    }
                )
            }
        }

        CommonButton(
            text = "Change Language",
            onClick = {
                viewModel.changeLanguage(selectedLanguage)
            }
        )
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
    LanguageScreen(nacontroller = NavController(LocalContext.current))
}