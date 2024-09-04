package com.example.myshopmanagerapp.feature_app.security


interface HashService {

    suspend fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash
    suspend fun verify(password: String, salt: String, hashedPassword: String): Boolean

}