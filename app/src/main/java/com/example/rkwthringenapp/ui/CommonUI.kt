package com.example.rkwthringenapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.example.rkwthringenapp.R
import com.example.rkwthringenapp.ui.theme.App_Accent_Orange

/**
 * Eine wiederverwendbare TopAppBar für die gesamte App, die das RKW-Logo anzeigt.
 * @param title Der Text, der als Titel in der Leiste angezeigt wird.
 * @param actions Optionale Aktionen (z.B. Buttons), die vor dem Logo angezeigt werden.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RkwAppBar(
    title: String,
    onNavigationClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            when {
                onNavigationClick != null -> {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Zurück",
                            tint = Color.White
                        )
                    }
                }
                onMenuClick != null -> {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menü",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        actions = {
            actions()
            Image(
                painter = painterResource(id = R.drawable.rkw_thueringen_wuerfel_grau),
                contentDescription = "RKW Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = App_Accent_Orange,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}
