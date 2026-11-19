package com.reeloop.app.data.local

import android.content.Context
import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.SesionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SesionRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : SesionRepository {

    private val prefs = context.getSharedPreferences("reloop_sesion", Context.MODE_PRIVATE)
    private val _usuario = MutableStateFlow(leer())
    override val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    override fun guardar(usuario: Usuario) {
        prefs.edit()
            .putString("id", usuario.id)
            .putString("nombre", usuario.nombre)
            .putString("correo", usuario.correo)
            .apply()
        _usuario.value = usuario
    }

    override fun cerrar() {
        prefs.edit().clear().apply()
        _usuario.value = null
    }

    private fun leer(): Usuario? {
        val id = prefs.getString("id", null) ?: return null
        return Usuario(
            id,
            prefs.getString("nombre", "").orEmpty(),
            prefs.getString("correo", "").orEmpty()
        )
    }
}