package com.reeloop.app.domain.repository

import com.reeloop.app.domain.model.Usuario
import kotlinx.coroutines.flow.StateFlow

// Quién está usando la app. null = nadie ha iniciado sesión.
interface SesionRepository {
    val usuario: StateFlow<Usuario?>
    fun guardar(usuario: Usuario)
    fun cerrar()
}
