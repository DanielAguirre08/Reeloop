package com.reeloop.app.domain.model

// Modelo de dominio: Kotlin puro, sin anotaciones de Room ni de Gson.
// Las fechas son milisegundos (epoch). 0 significa "todavía no ocurrió".
data class Objeto(
    val id: String = "",
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val conservacion: String,
    val peso: Double,
    val modalidad: String,
    val estado: String = Opciones.DISPONIBLE,
    val propietarioId: String = "",
    val propietarioNombre: String = "",
    val solicitanteId: String = "",
    val solicitanteNombre: String = "",
    val codigoEntrega: String = "",     // solo llega con valor al propietario
    val fechaPublicado: Long = 0L,
    val fechaSolicitado: Long = 0L,
    val fechaEntregado: Long = 0L
)
