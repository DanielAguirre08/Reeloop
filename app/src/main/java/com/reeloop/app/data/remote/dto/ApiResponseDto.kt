package com.reeloop.app.data.remote.dto

// Misma envoltura que la API del profesor, más "message" para explicar los errores.
data class ApiResponseDto<T>(
    val success: Boolean,
    val data: T?,
    val message: String?,
    val timestamp: String?
)
