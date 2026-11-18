package com.reeloop.app.data.remote.api

import com.reeloop.app.data.remote.dto.AccionRequestDto
import com.reeloop.app.data.remote.dto.ApiResponseDto
import com.reeloop.app.data.remote.dto.EliminadoDto
import com.reeloop.app.data.remote.dto.ObjetoDto
import com.reeloop.app.data.remote.dto.ObjetosDataDto
import com.reeloop.app.data.remote.dto.PublicarRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ObjetoApiService {
    @GET("objetos")
    suspend fun getObjetos(@Query("usuarioId") usuarioId: String): ApiResponseDto<ObjetosDataDto>

    @GET("objetos/{id}")
    suspend fun getObjetoById(
        @Path("id") id: String,
        @Query("usuarioId") usuarioId: String
    ): ApiResponseDto<ObjetoDto>

    @POST("objetos")
    suspend fun publicar(@Body body: PublicarRequestDto): ApiResponseDto<ObjetoDto>

    @DELETE("objetos/{id}")
    suspend fun eliminar(
        @Path("id") id: String,
        @Query("usuarioId") usuarioId: String
    ): ApiResponseDto<EliminadoDto>

    @POST("objetos/{id}/solicitar")
    suspend fun solicitar(@Path("id") id: String, @Body body: AccionRequestDto): ApiResponseDto<ObjetoDto>

    @POST("objetos/{id}/aceptar")
    suspend fun aceptar(@Path("id") id: String, @Body body: AccionRequestDto): ApiResponseDto<ObjetoDto>

    @POST("objetos/{id}/rechazar")
    suspend fun rechazar(@Path("id") id: String, @Body body: AccionRequestDto): ApiResponseDto<ObjetoDto>

    @POST("objetos/{id}/entregar")
    suspend fun entregar(@Path("id") id: String, @Body body: AccionRequestDto): ApiResponseDto<ObjetoDto>
}
