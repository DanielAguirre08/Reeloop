package com.reeloop.app.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.reeloop.app.domain.model.Objeto
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetoDao {
    @Query("SELECT * FROM objetos ORDER BY fechaPublicado DESC")
    fun getObjetos(): Flow<List<Objeto>>

    @Insert
    suspend fun insert(objeto: Objeto): Long

    @Update
    suspend fun update(objeto: Objeto)

    @Delete
    suspend fun delete(objeto: Objeto)
}