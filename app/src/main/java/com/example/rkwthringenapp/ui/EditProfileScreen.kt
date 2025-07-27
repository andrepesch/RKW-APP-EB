package com.example.rkwthringenapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rkwthringenapp.R
import com.example.rkwthringenapp.data.UpdateProfileRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val profileViewModel: ProfileViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()
    val uiState by profileViewModel.uiState.collectAsState()

    var salutation by remember { mutableStateOf("Herr") }
    val salutations = listOf("Herr", "Frau", "Divers")
    var expanded by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var photoBase64 by remember { mutableStateOf<String?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bytes = navController.context.contentResolver.openInputStream(it)?.readBytes()
            photoBase64 = bytes?.let { b -> android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT) }
        }
    }

    LaunchedEffect(uiState.profile) {
        uiState.profile?.let { profile ->
            salutation = profile.salutation ?: "Herr"
            firstName = profile.firstName ?: ""
            lastName = profile.lastName ?: ""
            phone = profile.phone ?: ""
            address = profile.address ?: ""
            photoBase64 = profile.photo
        }
    }

    LaunchedEffect(authState.beraterId) {
        authState.beraterId?.let { profileViewModel.loadProfile(it) }
    }

    uiState.error?.let { error ->
        AlertDialog(
            onDismissRequest = { profileViewModel.clearMessages() },
            title = { Text("Fehler") },
            text = { Text(error) },
            confirmButton = { Button(onClick = { profileViewModel.clearMessages() }) { Text("OK") } }
        )
    }
    uiState.info?.let { info ->
        AlertDialog(
            onDismissRequest = { profileViewModel.clearMessages() },
            title = { Text("Info") },
            text = { Text(info) },
            confirmButton = { Button(onClick = { profileViewModel.clearMessages() }) { Text("OK") } }
        )
    }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Passwort ändern") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Neues Passwort") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            autoCorrect = false
                        )
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Bestätigen") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            autoCorrect = false
                        ),
                        isError = newPassword != confirmPassword && confirmPassword.isNotEmpty()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newPassword == confirmPassword && newPassword.length >= 8) {
                            authState.beraterId?.let { profileViewModel.changePassword(it, newPassword) }
                            showPasswordDialog = false
                            newPassword = ""
                            confirmPassword = ""
                        }
                    }
                ) { Text("Passwort ändern") }
            },
            dismissButton = { TextButton(onClick = { showPasswordDialog = false }) { Text("Abbrechen") } }
        )
    }

    Scaffold(
        topBar = {
            RkwAppBar(
                title = "Benutzerdaten",
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = salutation,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Anrede") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    salutations.forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            salutation = option
                            expanded = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Vorname") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nachname") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefon") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Adresse") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { imageLauncher.launch("image/*") }) {
                Text(if (photoBase64 != null) "Foto ändern" else "Foto auswählen")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    authState.beraterId?.let {
                        profileViewModel.updateProfile(
                            UpdateProfileRequest(
                                beraterId = it,
                                salutation = salutation,
                                firstName = firstName,
                                lastName = lastName,
                                phone = phone,
                                address = address,
                                photo = photoBase64
                            )
                        )
                        authViewModel.updateLocalUserData(salutation, lastName)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Speichern") }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = { showPasswordDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Passwort ändern")
            }
        }
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}