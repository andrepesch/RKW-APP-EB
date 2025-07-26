package com.example.rkwthringenapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rkwthringenapp.R

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
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            onNavigationClick?.let { callback ->
                IconButton(onClick = callback) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück"
                    )
                }
            }
        },
        actions = {
            actions()
            Image(
                painter = painterResource(id = R.drawable.rkw_thueringen_wuerfel_grau),
                contentDescription = "RKW Logo",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}
