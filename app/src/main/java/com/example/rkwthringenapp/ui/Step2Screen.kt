package com.example.rkwthringenapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rkwthringenapp.ui.util.DateVisualTransformation
import com.example.rkwthringenapp.ui.util.TaxIdVisualTransformation
import android.widget.Toast

@Composable
fun Step2Screen(
    navController: NavController,
    viewModel: RkwFormViewModel,
    authViewModel: AuthViewModel
) {
    val formData by viewModel.uiState.collectAsState()
    val taxIdErrors by viewModel.taxIdErrors.collectAsState()
    val dateErrors by viewModel.beneficialOwnerDateErrors.collectAsState()
    val isMainContactEmailError by viewModel.isMainContactEmailError.collectAsState()
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
                text = {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator()
                    }
                },
                confirmButton = {}
            )
        }
        is DeleteResult.Success -> {
            LaunchedEffect(result) {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                navController.navigate("dashboard") {
                    popUpTo("dashboard") { inclusive = true }
                }
                viewModel.clearDeleteResult()
            }
        }
        is DeleteResult.Error -> {
            AlertDialog(
                onDismissRequest = { viewModel.clearDeleteResult() },
                title = { Text("Fehler beim Löschen") },
                text = { Text(result.message) },
                confirmButton = {
                    Button(onClick = { viewModel.clearDeleteResult() }) { Text("OK") }
                }
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

    val legalFormsRequiringBeneficialOwners = listOf(
        "GmbH", "GmbH & Co. KG", "UG (haftungsbeschränkt)", "Kommanditgesellschaft (KG)",
        "Offene Handelsgesellschaft (OHG)", "Aktiengesellschaft (AG)", "Limited (Ltd.)",
        "Ltd. & Co. KG", "Eingetragene Genossenschaft (eG)", "KG auf Aktien (KGaA)",
        "Partnerschaftsgesellschaft", "Societas Europaea (SE)", "Stiftung"
    )
    val showBeneficialOwners = formData.legalForm in legalFormsRequiringBeneficialOwners

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
            ProgressStepper(currentStep = 2, stepLabels = stepLabels)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Ansprechpartner für RKW", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(value = formData.mainContact.name, onValueChange = { viewModel.updateMainContactName(it) }, label = { Text("Vor- und Nachname") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = formData.mainContact.email,
                onValueChange = { viewModel.updateMainContactEmail(it) },
                label = { Text("E-Mail") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = isMainContactEmailError,
                supportingText = { if (isMainContactEmailError) Text("E-Mail-Adresse ungültig") }
            )
            OutlinedTextField(value = formData.mainContact.phone, onValueChange = { viewModel.updateMainContactPhone(it) }, label = { Text("Telefon") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())

            AnimatedVisibility(visible = showBeneficialOwners) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Wirtschaftlich berechtigte Personen", style = MaterialTheme.typography.titleLarge)
                    InfoBox(text = "Wirtschaftlich berechtigte Personen gemäß § 3 GwG (Eintragung im Transparenzregister): Nicht automatisch vertretungsberechtigte Personen. Maßgeblich ist die tatsächliche Kontrolle, z. B. durch Kapital- oder Stimmrechte.")

                    formData.beneficialOwners.forEachIndexed { index, owner ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Person ${index + 1}", style = MaterialTheme.typography.titleMedium)
                                    IconButton(onClick = { viewModel.removeBeneficialOwner(index) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Person entfernen")
                                    }
                                }
                                OutlinedTextField(value = owner.firstName, onValueChange = { viewModel.updateBeneficialOwner(index, owner.copy(firstName = it)) }, label = { Text("Vorname") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(value = owner.lastName, onValueChange = { viewModel.updateBeneficialOwner(index, owner.copy(lastName = it)) }, label = { Text("Nachname") }, modifier = Modifier.fillMaxWidth())
                                OutlinedTextField(
                                    value = owner.birthDate,
                                    onValueChange = { viewModel.updateBeneficialOwner(index, owner.copy(birthDate = it)) },
                                    label = { Text("Geburtsdatum (TTMMJJJJ)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = DateVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    isError = dateErrors[index] == true,
                                    supportingText = { if (dateErrors[index] == true) Text("Datum ungültig") }
                                )
                                OutlinedTextField(
                                    value = owner.taxId,
                                    onValueChange = { viewModel.updateBeneficialOwner(index, owner.copy(taxId = it)) },
                                    label = { Text("Steuerliche Identifikationsnummer") },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = TaxIdVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    isError = taxIdErrors[index] == true,
                                    supportingText = { if (taxIdErrors[index] == true) Text("Steuer-ID ungültig") }
                                )
                            }
                        }
                    }
                    Button(onClick = { viewModel.addBeneficialOwner() }, modifier = Modifier.fillMaxWidth()) {
                        Text("+ Weitere Person hinzufügen")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.popBackStack() }) { Text("Zurück") }
                Button(onClick = { navController.navigate("step3") }) { Text("Weiter") }
            }
        }
    }
}
