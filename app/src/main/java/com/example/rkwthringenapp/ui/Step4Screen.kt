package com.example.rkwthringenapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rkwthringenapp.ui.util.CurrencyVisualTransformation
import com.example.rkwthringenapp.ui.util.DateVisualTransformation
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step4Screen(
    navController: NavController,
    viewModel: RkwFormViewModel,
    authViewModel: AuthViewModel
) {
    val formData by viewModel.uiState.collectAsState()
    val details = formData.consultationDetails
    val isDailyRateError by viewModel.isDailyRateError.collectAsState()
    val isEndDateError by viewModel.isEndDateError.collectAsState()
    val stepLabels = listOf("Unternehmensdaten", "Ansprechpartner", "Finanzdaten", "Beratung", "Berater", "Abschluss")

    val focusOptions = listOf(
        "Strategie und Geschäftsideen",
        "Finanzierung und Investitionen",
        "Unternehmenswachstum und Wettbewerbsfähigkeit",
        "Internationalisierung",
        "Rationalisierungsmaßnahmen und Kostensenkungen, Technologietransfer und Technologieanwendung",
        "Personalmanagement/ Organisationsentwicklung",
        "Produktportfolio, Marktanalysen und Marketing",
        "Innovationsmanagement",
        "Materialeffizienz",
        "Unternehmensnachfolge",
        "Kooperation von Unternehmen",
        "Nachhaltigkeit und Anpassung an den Klimawandel",
        "Prozessbegleitung",
        "Qualitätsmanagement",
        "Digitalisierung"
    )
    val scopeOptions = (6..25).map { "$it Tage" }
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
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) { CircularProgressIndicator() }
                },
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

    var focusDropdownExpanded by remember { mutableStateOf(false) }
    var scopeDropdownExpanded by remember { mutableStateOf(false) }

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
            ProgressStepper(currentStep = 4, stepLabels = stepLabels)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Details zur Beratung", style = MaterialTheme.typography.titleLarge)

            // --- DIESER GANZE BLOCK HAT GEFEHLT ---
            ExposedDropdownMenuBox(
                expanded = focusDropdownExpanded,
                onExpandedChange = { focusDropdownExpanded = !focusDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = details.focus.ifEmpty { "Bitte auswählen" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Schwerpunkt") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = focusDropdownExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = focusDropdownExpanded,
                    onDismissRequest = { focusDropdownExpanded = false }
                ) {
                    focusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.updateConsultationFocus(option)
                                focusDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(
                    expanded = scopeDropdownExpanded,
                    onExpandedChange = { scopeDropdownExpanded = !scopeDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = if (details.scopeInDays == 0) "Bitte auswählen" else "${details.scopeInDays} Tage",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Umfang") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = scopeDropdownExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = scopeDropdownExpanded,
                        onDismissRequest = { scopeDropdownExpanded = false }
                    ) {
                        scopeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.updateConsultationScope(option)
                                    scopeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = details.dailyRate,
                    onValueChange = { viewModel.updateConsultationRate(it) },
                    label = { Text("Tagessatz in €") },
                    modifier = Modifier.weight(1f),
                    visualTransformation = CurrencyVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isDailyRateError,
                    supportingText = { if (isDailyRateError) Text("Mind. 600 €") }
                )
            }
            // --- ENDE DES FEHLENDEN BLOCKS ---

            OutlinedTextField(
                value = details.endDate,
                onValueChange = { viewModel.updateConsultationEndDate(it) },
                label = { Text("Zeitraum bis (TTMMJJJJ)") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = DateVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isEndDateError,
                supportingText = { if (isEndDateError) Text("Datum ungültig") }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = details.initialSituation,
                onValueChange = { viewModel.updateConsultationInitialSituation(it) },
                label = { Text("Ausgangssituation und Problembeschreibung") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            OutlinedTextField(
                value = details.consultationContent,
                onValueChange = { viewModel.updateConsultationContent(it) },
                label = { Text("Beratungsinhalt: Vorgehen, Ziele, Mehrwert") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.popBackStack() }) { Text("Zurück") }
                Button(onClick = { navController.navigate("step5") }) { Text("Weiter") }
            }
        }
    }
}