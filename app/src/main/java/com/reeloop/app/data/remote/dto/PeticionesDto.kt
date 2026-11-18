package com.reeloop.app.data.remote.dto

// Cuerpos (body) que la app ENVÍA al servidor.

data class LoginRequestDto(
    val correo: String,
    val clave: String
)

data class RegistroRequestDto(
    val nombre: String,
    val correo: String,
    val clave: String
)

data class PublicarRequestDto(
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val conservacion: String,
    val peso: Double,
    val modalidad: String,
    val propietarioId: String,
    val propietarioNombre: String
)

// Sirve para solicitar, aceptar, rechazar y entregar. Gson no envía los campos null.
data class AccionRequestDto(
    val usuarioId: String,
    val usuarioNombre: String? = null,
    val codigo: String? = null
)

data class EliminadoDto(
    val id: String?
)
