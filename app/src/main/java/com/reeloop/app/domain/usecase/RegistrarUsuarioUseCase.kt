package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.AuthRepository
import com.reeloop.app.domain.repository.SesionRepository
import com.reeloop.app.util.Validacion
import javax.inject.Inject

class RegistrarUsuarioUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sesion: SesionRepository
) {
    suspend operator fun invoke(nombre: String, correo: String, clave: String): Result<Usuario> {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim().lowercase()
        if (nombreLimpio.length < 3) return Result.failure(Exception("Escribe tu nombre completo"))
        if (!Validacion.correoValido(correoLimpio)) return Result.failure(Exception("Ingresa un correo válido"))
        if (!Validacion.claveValida(clave)) {
            return Result.failure(Exception("La contraseña debe tener al menos ${Validacion.CLAVE_MINIMA} caracteres"))
        }
        return repository.registrar(nombreLimpio, correoLimpio, clave).onSuccess { sesion.guardar(it) }
    }
}
