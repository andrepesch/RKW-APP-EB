package com.example.rkwthringenapp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rkwthringenapp.data.FormSummary
import com.example.rkwthringenapp.ui.theme.*

@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    rkwFormViewModel: RkwFormViewModel
) {
    val dashboardViewModel: DashboardViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val shareResult by dashboardViewModel.shareResult.collectAsState()
    val context = LocalContext.current

    // Reagiert auf das Ergebnis der "Teilen"-Aktion
    when (val result = shareResult) {
        is ShareResult.Loading -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Einladung wird gesendet...") },
                text = { Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) { CircularProgressIndicator() } },
                confirmButton = {}
            )
        }
        is ShareResult.Success -> {
            LaunchedEffect(result) {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                dashboardViewModel.clearShareResult()
            }
        }
        is ShareResult.Error -> {
            AlertDialog(
                onDismissRequest = { dashboardViewModel.clearShareResult() },
                title = { Text("Fehler beim Teilen") },
                text = { Text(result.message) },
                confirmButton = { Button(onClick = { dashboardViewModel.clearShareResult() }) { Text("OK") } }
            )
        }
        is ShareResult.Idle -> {}
    }

    LaunchedEffect(authState.beraterId) {
        authState.beraterId?.let { dashboardViewModel.loadForms(it) }
    }

    var searchQuery by remember { mutableStateOf("") }

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Box {
                RkwAppBar(
                    title = "RKW Thüringen",
                    onMenuClick = { menuExpanded = true }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Benutzerdaten ändern") },
                        onClick = {
                            menuExpanded = false
                            navController.navigate("editProfile")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ausloggen") },
                        onClick = {
                            menuExpanded = false
                            authViewModel.logout()
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    rkwFormViewModel.startNewForm()
                    navController.navigate("step1")
                },
                shape = MaterialTheme.shapes.medium,
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Neuen Bogen anlegen",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val greeting = "Hallo " + listOfNotNull(authState.salutation, authState.lastName).joinToString(" ")
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = "Was möchten Sie tun?",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Firma suchen…") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            when {
                dashboardState.isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                dashboardState.error != null -> Text(
                    "Fehler: ${'$'}{dashboardState.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                dashboardState.forms.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Keine Bögen gefunden.") }
                else -> {
                    val filtered = dashboardState.forms.filter { it.companyName.contains(searchQuery, ignoreCase = true) }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filtered) { form ->
                            FormCard(
                                form = form,
                                onClick = {
                                    if (form.status == "entwurf") {
                                        rkwFormViewModel.loadDraft(form.id)
                                        navController.navigate("step1")
                                    } else {
                                        navController.navigate("sentFormDetail/${form.id}")
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormCard(form: FormSummary, onClick: () -> Unit) {
    val isDraft = form.status == "entwurf"

    val bottomBarColor = if (isDraft) App_Status_Entwurf_Bar else App_Status_Gesendet_Bar
    val bottomTextColor = if (isDraft) App_Text_White else App_Text_Black

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Badge with scope and rate in the top-right corner
            if (form.scopeInDays > 0 && form.dailyRate > 0) {
                Text(
                    text = "${form.scopeInDays} TW zu ${form.dailyRate} EUR",
                    color = App_Text_White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = App_Accent_Orange,
                            shape = RoundedCornerShape(bottomStart = 12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 48.dp)) {
                Text(
                    text = form.companyName,
                    style = MaterialTheme.typography.titleMedium,
                    color = App_Text_Black,
                    fontWeight = FontWeight.Bold
                )
                if (form.address.isNotBlank()) {
                    Text(
                        text = extractCity(form.address),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = App_Text_Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Bottom status bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(bottomBarColor)
                    .padding(horizontal = 20.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isDraft) "Entwurf" else "Gesendet",
                    color = bottomTextColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
    }
}

private fun extractCity(address: String): String {
    val afterComma = address.substringAfterLast(",").trim()
    val parts = afterComma.split(" ")
    return if (parts.isNotEmpty()) parts.last() else afterComma
}
