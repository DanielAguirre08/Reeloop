package com.reeloop.app.data.fake

import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

// Reemplazo en memoria de la nube. Aplica las mismas reglas que la Lambda reloop_auth.
@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {

    private data class Cuenta(val usuario: Usuario, val clave: String)

    private val cuentas = mutableListOf(
        Cuenta(DatosFake.ana, DatosFake.CLAVE_DEMO),
        Cuenta(DatosFake.carlos, DatosFake.CLAVE_DEMO)
    )

    override suspend fun registrar(nombre: String, correo: String, clave: String): Result<Usuario> {
        delay(DatosFake.RETARDO_MS)
        if (cuentas.any { it.usuario.correo == correo }) {
            return Result.failure(Exception("Ya existe una cuenta con ese correo"))
        }
        val usuario = Usuario(id = "u-${cuentas.size + 1}", nombre = nombre, correo = correo)
        cuentas.add(Cuenta(usuario, clave))
        return Result.success(usuario)
    }

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Usuario> {
        delay(DatosFake.RETARDO_MS)
        val cuenta = cuentas.find { it.usuario.correo == correo && it.clave == clave }
            ?: return Result.failure(Exception("Correo o contraseña incorrectos"))
        return Result.success(cuenta.usuario)
    }
}
