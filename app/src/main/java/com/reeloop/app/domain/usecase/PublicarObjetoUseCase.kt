package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.domain.repository.ObjetoRepository
import com.reeloop.app.domain.repository.SesionRepository
import javax.inject.Inject

class PublicarObjetoUseCase @Inject constructor(
    private val repository: ObjetoRepository,
    private val sesion: SesionRepository
) {
    // peso llega ya convertido a número; null significa que el texto no era válido.
    suspend operator fun invoke(
        nombre: String,
        descripcion: String,
        categoria: String,
        conservacion: String,
        peso: Double?,
        modalidad: String
    ): Result<Objeto> {
        val usuario = sesion.usuarioActual().getOrElse { return Result.failure(it) }
        val error = when {
            nombre.isBlank() -> "Escribe el nombre del objeto"
            descripcion.isBlank() -> "Escribe una descripción"
            peso == null || peso <= 0.0 -> "Ingresa un peso válido"
            categoria !in Opciones.categorias -> "Elige una categoría"
            conservacion !in Opciones.conservaciones -> "Elige el estado de conservación"
            modalidad !in Opciones.modalidades -> "Elige una modalidad"
            else -> null
        }
        if (error != null) return Result.failure(Exception(error))

        return repository.publicar(
            Objeto(
                nombre = nombre.trim(),
                descripcion = descripcion.trim(),
                categoria = categoria,
                conservacion = conservacion,
                peso = peso ?: 0.0,
                modalidad = modalidad,
                propietarioId = usuario.id,
                propietarioNombre = usuario.nombre
            )
        )
    }
}
