package com.example.rkwthringenapp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rkwthringenapp.data.FormSummary
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("RKW Thüringen") },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Einstellungen")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                rkwFormViewModel.startNewForm()
                navController.navigate("step1")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Neuen Bogen anlegen")
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
                                        navController.navigate("sentFormDetail/${'$'}{form.id}")
                                    }
                                },
                                onShareClick = { if (form.status == "entwurf") dashboardViewModel.shareForm(form.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormCard(form: FormSummary, onClick: () -> Unit, onShareClick: () -> Unit) {
    val isDraft = form.status == "entwurf"
    val container = if (isDraft) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    val textColor = if (isDraft) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = form.companyName,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (isDraft) {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Outlined.Share, contentDescription = "Mit Kunde teilen", tint = textColor)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(formatDate(form.updated_at), style = MaterialTheme.typography.bodySmall, color = textColor)
                Text(if (isDraft) "Entwurf" else "Abgesendet", style = MaterialTheme.typography.bodySmall, color = textColor)
            }
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY)
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        formatter.format(parser.parse(dateString)!!)
    } catch (e: Exception) {
        dateString
    }
}