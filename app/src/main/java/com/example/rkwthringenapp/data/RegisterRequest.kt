package com.example.rkwthringenapp.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val salutation: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val address: String,
    val photo: String? = null
)