package com.example.rkwthringenapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast

@Composable
fun Step5Screen(
    navController: NavController,
    viewModel: RkwFormViewModel,
    authViewModel: AuthViewModel
) {
    val formData by viewModel.uiState.collectAsState()
    val consultantEmailErrors by viewModel.consultantEmailErrors.collectAsState()
    val stepLabels = listOf("Unternehmensdaten", "Ansprechpartner", "Finanzdaten", "Beratung", "Berater", "Abschluss")
    val authState by authViewModel.uiState.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val deleteResult by viewModel.deleteResult.collectAsState()
    val context = LocalContext.current

    when (val result = deleteResult) {
        is DeleteResult.Loading -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Löschen...") },
                text = { Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) { CircularProgressIndicator() } },
                confirmButton = {}
            )
        }
        is DeleteResult.Success -> {
            LaunchedEffect(result) {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } }
                viewModel.clearDeleteResult()
            }
        }
        is DeleteResult.Error -> {
            AlertDialog(
                onDismissRequest = { viewModel.clearDeleteResult() },
                title = { Text("Fehler beim Löschen") },
                text = { Text(result.message) },
                confirmButton = { Button(onClick = { viewModel.clearDeleteResult() }) { Text("OK") } }
            )
        }
        is DeleteResult.Idle -> {}
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Erfassungsbogen löschen") },
            text = { Text("Möchten Sie den Erfassungsbogen wirklich löschen?") },
            confirmButton = {
                Button(onClick = {
                    showDeleteConfirm = false
                    formData.id?.let { viewModel.deleteForm(it) }
                }) {
                    Text("Erfassungsbogen löschen", maxLines = 1)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) { Text("Abbrechen") }
            }
        )
    }

    Scaffold(
        topBar = {
            Box {
                RkwAppBar(
                    title = "Erfassungsbogen",
                    onMenuClick = { menuExpanded = true }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Zurück zum Dashboard") },
                        onClick = {
                            menuExpanded = false
                            navController.navigate("dashboard") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Entwurf speichern") },
                        onClick = {
                            menuExpanded = false
                            authState.beraterId?.let { viewModel.saveForm("entwurf", it) }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Erfassungsbogen löschen") },
                        onClick = {
                            menuExpanded = false
                            showDeleteConfirm = true
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ProgressStepper(currentStep = 5, stepLabels = stepLabels)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Beraterauswahl", style = MaterialTheme.typography.titleLarge)

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = !formData.hasChosenConsultant,
                            onClick = { viewModel.updateHasChosenConsultant(false) }
                        )
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = !formData.hasChosenConsultant,
                        onClick = { viewModel.updateHasChosenConsultant(false) }
                    )
                    Text(
                        text = "Ich wünsche eine Empfehlung für ein Beratungsunternehmen vom RKW Thüringen.",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = formData.hasChosenConsultant,
                            onClick = { viewModel.updateHasChosenConsultant(true) }
                        )
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = formData.hasChosenConsultant,
                        onClick = { viewModel.updateHasChosenConsultant(true) }
                    )
                    Text(
                        text = "Ich habe mich bereits für ein Beratungsunternehmen entschieden.",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            AnimatedVisibility(visible = formData.hasChosenConsultant) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = formData.consultingFirm,
                        onValueChange = { viewModel.updateConsultingFirm(it) },
                        label = { Text("Beratungsfirma") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    formData.consultants.forEachIndexed { index, consultant ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Berater/in ${index + 1}", style = MaterialTheme.typography.titleMedium)
                                    IconButton(onClick = { viewModel.removeConsultant(index) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Berater entfernen")
                                    }
                                }
                                OutlinedTextField(value = consultant.firstName, onValueChange = { viewModel.updateConsultant(index, consultant.copy(firstName = it)) }, label = { Text("Vorname") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = consultant.lastName, onValueChange = { viewModel.updateConsultant(index, consultant.copy(lastName = it)) }, label = { Text("Nachname") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = consultant.accreditationId, onValueChange = { viewModel.updateConsultant(index, consultant.copy(accreditationId = it)) }, label = { Text("Akkreditierungs-Nr.") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(
                                    value = consultant.email,
                                    onValueChange = { viewModel.updateConsultant(index, consultant.copy(email = it)) },
                                    label = { Text("E-Mail") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = consultantEmailErrors[index] == true,
                                    supportingText = { if (consultantEmailErrors[index] == true) Text("E-Mail-Adresse ungültig") }
                                )
                            }
                        }
                    }

                    if (formData.consultants.size < 2) {
                        Button(
                            onClick = { viewModel.addConsultant() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Weitere/n Berater/in hinzufügen")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.popBackStack() }) { Text("Zurück") }
                Button(onClick = { navController.navigate("step6") }) { Text("Weiter") }
            }
        }
    }
}
