package com.reeloop.app.data.repository

import com.reeloop.app.data.remote.api.ObjetoApiService
import com.reeloop.app.data.remote.dto.AccionRequestDto
import com.reeloop.app.data.remote.dto.PublicarRequestDto
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.ObjetoRepository
import javax.inject.Inject

class ObjetoRepositoryImpl @Inject constructor(
    private val api: ObjetoApiService
) : ObjetoRepository {

    override suspend fun getObjetos(usuarioId: String): Result<List<Objeto>> =
        llamar({ api.getObjetos(usuarioId) }) { datos -> datos.items.orEmpty().map { it.toDomain() } }

    override suspend fun getObjetoById(id: String, usuarioId: String): Result<Objeto> =
        llamar({ api.getObjetoById(id, usuarioId) }) { it.toDomain() }

    override suspend fun publicar(objeto: Objeto): Result<Objeto> =
        llamar({
            api.publicar(
                PublicarRequestDto(
                    nombre = objeto.nombre,
                    descripcion = objeto.descripcion,
                    categoria = objeto.categoria,
                    conservacion = objeto.conservacion,
                    peso = objeto.peso,
                    modalidad = objeto.modalidad,
                    propietarioId = objeto.propietarioId,
                    propietarioNombre = objeto.propietarioNombre
                )
            )
        }) { it.toDomain() }

    override suspend fun eliminar(id: String, usuarioId: String): Result<Unit> =
        llamar({ api.eliminar(id, usuarioId) }) { }

    override suspend fun solicitar(id: String, solicitante: Usuario): Result<Objeto> =
        llamar({ api.solicitar(id, AccionRequestDto(solicitante.id, usuarioNombre = solicitante.nombre)) }) { it.toDomain() }

    override suspend fun aceptar(id: String, usuarioId: String): Result<Objeto> =
        llamar({ api.aceptar(id, AccionRequestDto(usuarioId)) }) { it.toDomain() }

    override suspend fun rechazar(id: String, usuarioId: String): Result<Objeto> =
        llamar({ api.rechazar(id, AccionRequestDto(usuarioId)) }) { it.toDomain() }

    override suspend fun entregar(id: String, codigo: String, usuarioId: String): Result<Objeto> =
        llamar({ api.entregar(id, AccionRequestDto(usuarioId, codigo = codigo)) }) { it.toDomain() }
}
