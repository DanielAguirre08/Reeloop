package com.reeloop.app.data.repository

import com.reeloop.app.data.remote.api.AuthApiService
import com.reeloop.app.data.remote.dto.LoginRequestDto
import com.reeloop.app.data.remote.dto.RegistroRequestDto
import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {

    override suspend fun registrar(nombre: String, correo: String, clave: String): Result<Usuario> =
        llamar({ api.registrar(RegistroRequestDto(nombre, correo, clave)) }) { it.toDomain() }

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Usuario> =
        llamar({ api.login(LoginRequestDto(correo, clave)) }) { it.toDomain() }
}
