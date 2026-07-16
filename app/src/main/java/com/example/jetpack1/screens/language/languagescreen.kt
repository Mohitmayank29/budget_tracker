package com.example.jetpack1.screens.language

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.jetpack1.Constants.constants
import com.example.jetpack1.R
import com.example.jetpack1.common.CommonButton
import com.example.jetpack1.common.DashboardTopBar
import com.example.jetpack1.common.LanguageItem
import com.example.jetpack1.enumclasses.TopBarType
import com.example.jetpack1.navigation.navroute
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController, viewModel: LanguageViewModel = hiltViewModel()) {

    val languages = listOf(
        LanguageItem("en", "English", "English"),
        LanguageItem("hi", "Hindi", "हिंदी"),
        LanguageItem("gu", "Gujarati", "ગુજરાતી"),
        LanguageItem("mr", "Marathi", "मराठी"),
        LanguageItem("pa", "Punjabi", "ਪੰਜਾਬੀ")
    )
    val context = LocalContext.current
    val configuration = LocalConfiguration.current // Add this

    val currentLocale = configuration.locales[0]?.language ?: "en"
    val savedLanguage = viewModel.getPreferenceDataStore(constants.savedLanguage)
        .collectAsStateWithLifecycle(initialValue = "en")
    val currentLanguageName = languages.find { it.id == currentLocale }?.name ?: "English"

    var selectedLanguage by remember {
        mutableStateOf("en")
    }

    LaunchedEffect(savedLanguage.value) {
        if (savedLanguage.value.isNotEmpty()) {
            selectedLanguage = savedLanguage.value
        }
        else{
            selectedLanguage = currentLocale
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        val comefromsetting : String = "1"
        DashboardTopBar(
            title = "Language",
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                rememberTopAppBarState()
            ),
            type = if(comefromsetting.equals("1",true)) TopBarType.BACK_ONLY else TopBarType.TITLE_ONLY,
            onbackclick = { navController.popBackStack()}
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(10.dp)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.select_language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(height = 4.dp))

            Text(
                text = "Current language: $currentLanguageName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
            Text(
                text = "You can change this later from settings.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                items(languages) { language ->
                    LanguageItemCard(
                        language = language,
                        isSelected = selectedLanguage == language.id,
                        isCurrentLanguage = currentLocale == language.id,
                        onSelect = {
                            selectedLanguage = language.id
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        Column(
            Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonButton(
                text = stringResource(id = R.string.save_language),
                onClick = {
                    viewModel.saveLanguage(
                        context = context,
                        languageCode = selectedLanguage
                    )
                    navController.navigate(navroute.Dashboard.route) {
                        popUpTo(navroute.language.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun LanguageItemCard(
    language: LanguageItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    isCurrentLanguage: Boolean = false
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onSelect() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = language.nativeName.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            isSelected || isCurrentLanguage -> Color.White
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = language.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (isCurrentLanguage && !isSelected) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(Current)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 10.sp
                    )
                }
            }

            RadioButton(
                selected = isSelected,
                onClick = onSelect
            )
        }
    }
}
@Preview
@Composable
private fun PreviewLanguageScreen() {
    LanguageScreen(navController = NavController(LocalContext.current))
}