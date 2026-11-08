package com.reeloop.app.domain.repository
import com.reeloop.app.domain.model.Objeto
import kotlinx.coroutines.flow.Flow
interface ObjetoRepository {
    fun getObjetos(): Flow<List<Objeto>>
    suspend fun insertar(objeto: Objeto): Long
    suspend fun actualizar(objeto: Objeto)
    suspend fun eliminar(objeto: Objeto)
}