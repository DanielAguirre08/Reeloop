package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.SesionRepository

internal const val SIN_SESION = "Inicia sesión para continuar"

// Devuelve el usuario con sesión activa o un Result.failure listo para retornar.
internal fun SesionRepository.usuarioActual(): Result<Usuario> {
    val actual = usuario.value
    return if (actual != null) Result.success(actual) else Result.failure(Exception(SIN_SESION))
}
