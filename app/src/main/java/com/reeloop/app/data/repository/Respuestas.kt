package com.reeloop.app.data.repository

import com.reeloop.app.data.remote.dto.ApiResponseDto
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

private const val ERROR_SERVIDOR = "Error en la respuesta del servidor"
private const val ERROR_RED = "Sin conexión. Revisa tu internet e inténtalo otra vez"

// Ejecuta la llamada y convierte la respuesta de la API en un Result, igual que
// el runCatching del ejemplo del profesor pero con un mensaje claro cuando no hay red.
internal suspend fun <D, R> llamar(
    peticion: suspend () -> ApiResponseDto<D>,
    convertir: (D) -> R
): Result<R> = try {
    val respuesta = peticion()
    val datos = respuesta.data
    if (respuesta.success && datos != null) {
        Result.success(convertir(datos))
    } else {
        Result.failure(Exception(respuesta.message ?: ERROR_SERVIDOR))
    }
} catch (e: CancellationException) {
    throw e   // la corrutina se canceló (el usuario salió de la pantalla): no es un error
} catch (e: IOException) {
    Result.failure(Exception(ERROR_RED))
} catch (e: Exception) {
    Result.failure(Exception(ERROR_SERVIDOR))
}
