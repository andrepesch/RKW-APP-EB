package com.example.rkwthringenapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import android.widget.Toast
import com.example.rkwthringenapp.ui.util.DateVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(
    navController: NavController,
    viewModel: RkwFormViewModel,
    authViewModel: AuthViewModel
) {
    val formData by viewModel.uiState.collectAsState()
    val isPlzError by viewModel.isPlzError.collectAsState()
    val isDateError by viewModel.isDateError.collectAsState()

    val legalForms = listOf("Einzelunternehmen", "GbR", "e.K.", "GmbH", "GmbH & Co. KG", "UG (haftungsbeschränkt)", "Freie Berufe", "Kommanditgesellschaft (KG)", "Offene Handelsgesellschaft (OHG)", "Aktiengesellschaft (AG)", "Limited (Ltd.)", "Ltd. & Co. KG", "e. V.", "Eingetragene Genossenschaft (eG)", "KG auf Aktien (KGaA)", "Partnerschaftsgesellschaft", "Societas Europaea (SE)", "Stiftung")
    val stepLabels = listOf("Unternehmensdaten", "Ansprechpartner", "Finanzdaten", "Beratung", "Berater", "Abschluss")

    var showWzSearchDialog by remember { mutableStateOf(false) }
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

    if (showWzSearchDialog) {
        WzSearchDialog(
            fullList = viewModel.wzList,
            onDismiss = { showWzSearchDialog = false },
            onItemSelected = {
                viewModel.onWzSelected(it)
                showWzSearchDialog = false
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
            ProgressStepper(currentStep = 1, stepLabels = stepLabels)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = formData.companyName, onValueChange = { viewModel.updateCompanyName(it) }, label = { Text("Unternehmensname") }, modifier = Modifier.fillMaxWidth())

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = formData.legalForm.ifEmpty { "Bitte auswählen" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rechtsform") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    // MODERNE VERSION VON menuAnchor
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    legalForms.forEach { form ->
                        DropdownMenuItem(
                            text = { Text(form) },
                            onClick = {
                                viewModel.updateLegalForm(form)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = formData.foundationDate,
                onValueChange = { viewModel.updateFoundationDate(it) },
                label = { Text("Gründungsdatum (TTMMJJJJ)") },
                visualTransformation = DateVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = isDateError,
                supportingText = { if (isDateError) Text("Datum ungültig", color = MaterialTheme.colorScheme.error) }
            )

            OutlinedTextField(value = formData.streetAndNumber, onValueChange = { viewModel.updateStreetAndNumber(it) }, label = { Text("Straße und Hausnummer") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = formData.postalCode, onValueChange = { viewModel.updatePostalCode(it) }, label = { Text("PLZ") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), isError = isPlzError, supportingText = { if (isPlzError) Text("PLZ ungültig", color = MaterialTheme.colorScheme.error) })
                OutlinedTextField(value = formData.city, onValueChange = { viewModel.updateCity(it) }, label = { Text("Ort") }, modifier = Modifier.weight(2f))
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { showWzSearchDialog = true },
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formData.industrySector.ifEmpty { "Bitte auswählen..." },
                        color = if (formData.industrySector.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Suchen",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Text("Vorsteuerabzugsberechtigt?", style = MaterialTheme.typography.bodyLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.selectable(selected = formData.isVatDeductible, onClick = { viewModel.updateIsVatDeductible(true) }).padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = formData.isVatDeductible, onClick = { viewModel.updateIsVatDeductible(true) })
                    Text("Ja", modifier = Modifier.padding(start = 4.dp))
                }
                Row(Modifier.selectable(selected = !formData.isVatDeductible, onClick = { viewModel.updateIsVatDeductible(false) }), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = !formData.isVatDeductible, onClick = { viewModel.updateIsVatDeductible(false) })
                    Text("Nein", modifier = Modifier.padding(start = 4.dp))
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = formData.hasWebsite, onCheckedChange = { viewModel.updateHasWebsite(it) })
                Text("Website vorhanden")
            }

            if (formData.hasWebsite) {
                OutlinedTextField(value = formData.websiteUrl, onValueChange = { viewModel.updateWebsiteUrl(it) }, label = { Text("Website/URL") }, modifier = Modifier.fillMaxWidth())
            }

            Button(onClick = { navController.navigate("step2") }, modifier = Modifier.align(Alignment.End).padding(bottom = 16.dp)) {
                Text("Weiter")
            }
        }
    }
}

@Composable
fun WzSearchDialog(
    fullList: List<String>,
    onDismiss: () -> Unit,
    onItemSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredList = if (searchQuery.isBlank()) {
        fullList
    } else {
        fullList.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f)) {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Suchen...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                    items(filteredList) { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = { onItemSelected(item) }
                        )
                    }
                }
            }
        }
    }
}