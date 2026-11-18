package com.reeloop.app.domain.repository

import com.reeloop.app.domain.model.Usuario

interface AuthRepository {
    suspend fun registrar(nombre: String, correo: String, clave: String): Result<Usuario>
    suspend fun iniciarSesion(correo: String, clave: String): Result<Usuario>
}
