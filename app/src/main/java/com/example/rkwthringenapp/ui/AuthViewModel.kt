package com.example.rkwthringenapp.ui

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rkwthringenapp.data.ApiClient
import com.example.rkwthringenapp.data.AuthRequest
import com.example.rkwthringenapp.data.LoginResponse
import com.example.rkwthringenapp.data.ServerResponse
import com.example.rkwthringenapp.data.RegisterRequest
import kotlinx.serialization.json.Json
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val info: String? = null,
    val beraterId: Int? = null,
    val salutation: String? = null,
    val lastName: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val loggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val beraterId = sharedPreferences.getInt("berater_id", -1).takeIf { it != -1 }
        val salutation = sharedPreferences.getString("salutation", null)
        val lastName = sharedPreferences.getString("last_name", null)
        _uiState.update {
            it.copy(
                isLoggedIn = loggedIn,
                beraterId = beraterId,
                salutation = salutation,
                lastName = lastName
            )
        }
    }

    fun testConnection() { /* ... unverändert ... */ }

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            _uiState.update { it.copy(error = "Bitte füllen Sie alle Felder aus.") }
            return
        }
        performAuthRequest(
            "https://formpilot.eu/login.php",
            trimmedEmail,
            trimmedPassword,
            isLogin = true
        )
    }

    fun register(
        email: String,
        password: String,
        salutation: String,
        firstName: String,
        lastName: String,
        phone: String,
        address: String,
        photo: String? = null
    ) {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _uiState.update { it.copy(error = "Bitte geben Sie eine gültige E-Mail-Adresse ein.") }
            return
        }
        if (trimmedPassword.length < 8 || firstName.isBlank() || lastName.isBlank()) {
            _uiState.update { it.copy(error = "Bitte füllen Sie alle Pflichtfelder korrekt aus.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response: HttpResponse = ApiClient.client.post("https://formpilot.eu/register.php") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        RegisterRequest(
                            email = trimmedEmail,
                            password = trimmedPassword,
                            salutation = salutation,
                            firstName = firstName,
                            lastName = lastName,
                            phone = phone,
                            address = address,
                            photo = photo
                        )
                    )
                }

                if (response.status == HttpStatusCode.OK) {
                    login(trimmedEmail, trimmedPassword)
                } else {
                    val body = response.bodyAsText()
                    val serverResponse = try {
                        Json.decodeFromString<ServerResponse>(body)
                    } catch (_: Exception) {
                        ServerResponse("error", body)
                    }
                    _uiState.update { it.copy(isLoading = false, error = serverResponse.message) }
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Unbekannter Fehler"
                _uiState.update { it.copy(isLoading = false, error = "Client-Fehler: $errorMsg") }
            }
        }
    }

    private fun performAuthRequest(url: String, email: String, password: String, isLogin: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response: HttpResponse = ApiClient.client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(AuthRequest(email, password))
                }

                if (response.status == HttpStatusCode.OK) {
                    if (isLogin) {
                        val body = response.bodyAsText()
                        val loginResponse = try {
                            Json.decodeFromString<LoginResponse>(body)
                        } catch (_: Exception) {
                            // Server hat keine gültige Login-Antwort geliefert
                            val serverMsg = try {
                                Json.decodeFromString<ServerResponse>(body).message
                            } catch (_: Exception) {
                                null
                            }
                            val msg = serverMsg ?: if (body.isNotBlank()) {
                                body
                            } else {
                                "Ungültige Antwort vom Server (Status ${response.status.value})"
                            }
                            _uiState.update { it.copy(isLoading = false, error = msg) }
                            return@launch
                        }
                        sharedPreferences.edit()
                            .putBoolean("isLoggedIn", true)
                            .putInt("berater_id", loginResponse.berater_id ?: -1)
                            .putString("salutation", loginResponse.salutation)
                            .putString("last_name", loginResponse.last_name)
                            .apply()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                beraterId = loginResponse.berater_id,
                                salutation = loginResponse.salutation,
                                lastName = loginResponse.last_name
                            )
                        }
                    } else {
                        login(email, password)
                    }
                } else {
                    val body = response.bodyAsText()
                    val serverResponse = try {
                        Json.decodeFromString<ServerResponse>(body)
                    } catch (_: Exception) {
                        ServerResponse("error", body)
                    }
                    _uiState.update { it.copy(isLoading = false, error = serverResponse.message) }
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Unbekannter Fehler"
                _uiState.update { it.copy(isLoading = false, error = "Client-Fehler: $errorMsg") }
            }
        }
    }

    fun logout() {
        sharedPreferences.edit()
            .putBoolean("isLoggedIn", false)
            .remove("berater_id")
            .remove("salutation")
            .remove("last_name")
            .apply()
        _uiState.update { AuthUiState() } // Setzt den kompletten Zustand zurück
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null, info = null) }
    }

    fun updateLocalUserData(salutation: String?, lastName: String?) {
        sharedPreferences.edit()
            .putString("salutation", salutation)
            .putString("last_name", lastName)
            .apply()
        _uiState.update { it.copy(salutation = salutation, lastName = lastName) }
    }
}