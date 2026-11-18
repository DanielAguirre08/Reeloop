package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.repository.ObjetoRepository
import com.reeloop.app.domain.repository.SesionRepository
import javax.inject.Inject

class SolicitarObjetoUseCase @Inject constructor(
    private val repository: ObjetoRepository,
    private val sesion: SesionRepository
) {
    suspend operator fun invoke(id: String): Result<Objeto> {
        val usuario = sesion.usuarioActual().getOrElse { return Result.failure(it) }
        return repository.solicitar(id, usuario)
    }
}
