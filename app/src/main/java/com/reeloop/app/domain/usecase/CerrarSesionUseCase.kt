package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.repository.SesionRepository
import javax.inject.Inject

class CerrarSesionUseCase @Inject constructor(private val sesion: SesionRepository) {
    operator fun invoke() = sesion.cerrar()
}
