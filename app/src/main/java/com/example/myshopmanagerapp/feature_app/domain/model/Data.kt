package com.example.myshopmanagerapp.feature_app.domain.model

data class Data(
    val companyContact: String,
    val companyLocation: String,
    val companyName: String,
    val companyOwners: String,
    val companyPersonnel: String,
    val companyProductAndServices: String,
    val dateCreated: Int,
    val email: String,
    val otherInfo: String,
    val password: String,
    val salt: String,
    val subscriptionEndDate: Int,
    val subscriptionPackage: String,
    val uniqueCompanyId: String
)