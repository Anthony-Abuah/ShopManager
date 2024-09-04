package com.example.myshopmanagerapp.feature_app.security

data class SaltedHash (
    val hash: String,
    val salt: String
)