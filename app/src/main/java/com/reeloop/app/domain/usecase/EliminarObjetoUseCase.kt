package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.repository.ObjetoRepository
import com.reeloop.app.domain.repository.SesionRepository
import javax.inject.Inject

class EliminarObjetoUseCase @Inject constructor(
    private val repository: ObjetoRepository,
    private val sesion: SesionRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        val usuario = sesion.usuarioActual().getOrElse { return Result.failure(it) }
        return repository.eliminar(id, usuario.id)
    }
}
