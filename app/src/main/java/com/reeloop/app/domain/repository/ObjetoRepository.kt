package com.reeloop.app.domain.repository

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Usuario

// usuarioId = quién hace la petición. El servidor lo usa para validar permisos
// y para decidir si devuelve el codigoEntrega.
interface ObjetoRepository {
    suspend fun getObjetos(usuarioId: String): Result<List<Objeto>>
    suspend fun getObjetoById(id: String, usuarioId: String): Result<Objeto>
    suspend fun publicar(objeto: Objeto): Result<Objeto>
    suspend fun eliminar(id: String, usuarioId: String): Result<Unit>
    suspend fun solicitar(id: String, solicitante: Usuario): Result<Objeto>
    suspend fun aceptar(id: String, usuarioId: String): Result<Objeto>
    suspend fun rechazar(id: String, usuarioId: String): Result<Objeto>
    suspend fun entregar(id: String, codigo: String, usuarioId: String): Result<Objeto>
}
