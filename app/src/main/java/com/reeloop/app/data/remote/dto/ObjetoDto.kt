package com.reeloop.app.data.remote.dto

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones

data class ObjetoDto(
    val id: String?,
    val nombre: String?,
    val descripcion: String?,
    val categoria: String?,
    val conservacion: String?,
    val peso: Double?,
    val modalidad: String?,
    val estado: String?,
    val propietarioId: String?,
    val propietarioNombre: String?,
    val solicitanteId: String?,
    val solicitanteNombre: String?,
    val codigoEntrega: String?,
    val fechaPublicado: Long?,
    val fechaSolicitado: Long?,
    val fechaEntregado: Long?
) {
    fun toDomain(): Objeto = Objeto(
        id = id.orEmpty(),
        nombre = nombre.orEmpty(),
        descripcion = descripcion.orEmpty(),
        categoria = categoria.orEmpty(),
        conservacion = conservacion.orEmpty(),
        peso = peso ?: 0.0,
        modalidad = modalidad.orEmpty(),
        estado = estado ?: Opciones.DISPONIBLE,
        propietarioId = propietarioId.orEmpty(),
        propietarioNombre = propietarioNombre.orEmpty(),
        solicitanteId = solicitanteId.orEmpty(),
        solicitanteNombre = solicitanteNombre.orEmpty(),
        codigoEntrega = codigoEntrega.orEmpty(),
        fechaPublicado = fechaPublicado ?: 0L,
        fechaSolicitado = fechaSolicitado ?: 0L,
        fechaEntregado = fechaEntregado ?: 0L
    )
}
