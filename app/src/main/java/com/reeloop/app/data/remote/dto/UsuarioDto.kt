package com.reeloop.app.data.remote.dto

import com.reeloop.app.domain.model.Usuario

data class UsuarioDto(
    val id: String?,
    val nombre: String?,
    val correo: String?
) {
    fun toDomain(): Usuario = Usuario(
        id = id.orEmpty(),
        nombre = nombre.orEmpty(),
        correo = correo.orEmpty()
    )
}
