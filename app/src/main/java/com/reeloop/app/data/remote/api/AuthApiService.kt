package com.reeloop.app.data.remote.api

import com.reeloop.app.data.remote.dto.ApiResponseDto
import com.reeloop.app.data.remote.dto.LoginRequestDto
import com.reeloop.app.data.remote.dto.RegistroRequestDto
import com.reeloop.app.data.remote.dto.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/registro")
    suspend fun registrar(@Body body: RegistroRequestDto): ApiResponseDto<UsuarioDto>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): ApiResponseDto<UsuarioDto>
}
