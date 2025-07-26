package com.example.rkwthringenapp.data

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val email: String,
    val salutation: String? = null,
    val firstName: String? = null,
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