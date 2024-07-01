package com.example.myshopmanagerapp.core

sealed class UIEvent{
        data class ShowSnackBar(val message: String): UIEvent()
    }