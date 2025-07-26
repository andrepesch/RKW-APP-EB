package com.example.rkwthringenapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rkwthringenapp.data.ApiClient
import com.example.rkwthringenapp.data.PasswordChangeRequest
import com.example.rkwthringenapp.data.UpdateProfileRequest
import com.example.rkwthringenapp.data.UserProfile
import com.example.rkwthringenapp.data.ServerResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val info: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile(beraterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val profile: UserProfile = ApiClient.client.get("https://formpilot.eu/get_user.php") {
                    parameter("berater_id", beraterId)
                }.body()
                _uiState.update { it.copy(isLoading = false, profile = profile) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Fehler: ${e.message}") }
            }
        }
    }

    fun updateProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, info = null) }
            try {
                val response: HttpResponse = ApiClient.client.post("https://formpilot.eu/update_user.php") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                val serverResp: ServerResponse = response.body()
                if (serverResp.status == "success") {
                    _uiState.update { it.copy(isLoading = false, info = serverResp.message) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = serverResp.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Client-Fehler: ${e.message}") }
            }
        }
    }

    fun changePassword(beraterId: Int, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, info = null) }
            try {
                val response: HttpResponse = ApiClient.client.post("https://formpilot.eu/change_password.php") {
                    contentType(ContentType.Application.Json)
                    setBody(PasswordChangeRequest(beraterId, password))
                }
                val serverResp: ServerResponse = response.body()
                if (serverResp.status == "success") {
                    _uiState.update { it.copy(isLoading = false, info = serverResp.message) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = serverResp.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Client-Fehler: ${e.message}") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, info = null) }
    }
}