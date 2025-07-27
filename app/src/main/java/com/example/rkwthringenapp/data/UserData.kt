package com.example.rkwthringenapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserProfile(
    val id: Int,
    val email: String,
    val salutation: String? = null,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val photo: String? = null
)

@Serializable
data class UpdateProfileRequest(
    val beraterId: Int,
    val salutation: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val address: String?,
    val photo: String? = null
)

@Serializable
data class PasswordChangeRequest(
    val beraterId: Int,
    val password: String
)