package com.reeloop.app.data

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.repository.ObjetoDao
import com.reeloop.app.domain.repository.ObjetoRepository
import kotlinx.coroutines.flow.Flow

class ObjetoRepositoryImpl(private val dao: ObjetoDao) : ObjetoRepository {
    override fun getObjetos(): Flow<List<Objeto>> = dao.getObjetos()
    override suspend fun insertar(objeto: Objeto): Long = dao.insert(objeto)
    override suspend fun actualizar(objeto: Objeto) = dao.update(objeto)
    override suspend fun eliminar(objeto: Objeto) = dao.delete(objeto)
}