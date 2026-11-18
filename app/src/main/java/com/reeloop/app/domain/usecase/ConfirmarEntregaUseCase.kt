package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.repository.ObjetoRepository
import com.reeloop.app.domain.repository.SesionRepository
import javax.inject.Inject

class ConfirmarEntregaUseCase @Inject constructor(
    private val repository: ObjetoRepository,
    private val sesion: SesionRepository
) {
    suspend operator fun invoke(id: String, codigo: String): Result<Objeto> {
        val usuario = sesion.usuarioActual().getOrElse { return Result.failure(it) }
        val codigoLimpio = codigo.trim()
        if (codigoLimpio.length != 6 || !codigoLimpio.all { it.isDigit() }) {
            return Result.failure(Exception("El código tiene 6 dígitos"))
        }
        return repository.entregar(id, codigoLimpio, usuario.id)
    }
}
