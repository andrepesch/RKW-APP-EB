package com.example.rkwthringenapp.ui

import android.util.Base64
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rkwthringenapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var salutation by remember { mutableStateOf("Herr") }
    val salutations = listOf("Herr", "Frau", "Divers")
    var expanded by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val context = LocalContext.current
    var photoBase64 by remember { mutableStateOf<String?>(null) }
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bytes = context.contentResolver.openInputStream(it)?.readBytes()
            photoBase64 = bytes?.let { b -> Base64.encodeToString(b, Base64.DEFAULT) }
        }
    }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val uiState by authViewModel.uiState.collectAsState()

    // Fehler-Dialog
    uiState.error?.let { error ->
        AlertDialog(
            onDismissRequest = { authViewModel.dismissError() },
            title = { Text("Fehler") },
            text = { Text(error) },
            confirmButton = {
                Button(onClick = { authViewModel.dismissError() }) {
                    Text("OK")
                }
            }
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hinzugefügtes RKW Logo
                Image(
                    painter = painterResource(id = R.drawable.rkw_thueringen_logo_grau),
                    contentDescription = "RKW Thüringen Logo",
                    modifier = Modifier.fillMaxWidth(0.7f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Angepasster Titel
                Text(
                    text = "Neues Beraterkonto erstellen",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Anrede Dropdown
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
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Nachname") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefon") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Adresse") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { imageLauncher.launch("image/*") }, enabled = !uiState.isLoading) {
                    Text(if (photoBase64 != null) "Foto ändern" else "Foto auswählen")
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-Mail") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Passwort (mind. 8 Zeichen)") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrectEnabled = false
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Passwort bestätigen") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrectEnabled = false
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    isError = password.trim() != confirmPassword.trim() && confirmPassword.isNotEmpty()
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        authViewModel.register(
                            email = email,
                            password = password,
                            salutation = salutation,
                            firstName = firstName,
                            lastName = lastName,
                            phone = phone,
                            address = address,
                            photo = photoBase64
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading &&
                        email.isNotBlank() &&
                        password.trim().length >= 8 &&
                        password.trim() == confirmPassword.trim() &&
                        firstName.isNotBlank() &&
                        lastName.isNotBlank()
                ) {
                    Text("Registrieren")
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { navController.navigateUp() },
                    enabled = !uiState.isLoading
                ) {
                    Text("Bereits registriert? Zum Login")
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}