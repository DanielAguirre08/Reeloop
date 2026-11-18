package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.AuthRepository
import com.reeloop.app.domain.repository.SesionRepository
import com.reeloop.app.util.Validacion
import javax.inject.Inject

class IniciarSesionUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sesion: SesionRepository
) {
    suspend operator fun invoke(correo: String, clave: String): Result<Usuario> {
        val correoLimpio = correo.trim().lowercase()
        if (!Validacion.correoValido(correoLimpio)) return Result.failure(Exception("Ingresa un correo válido"))
        if (clave.isBlank()) return Result.failure(Exception("Ingresa tu contraseña"))
        return repository.iniciarSesion(correoLimpio, clave).onSuccess { sesion.guardar(it) }
    }
}
