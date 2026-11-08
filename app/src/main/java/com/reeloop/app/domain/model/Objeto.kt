package com.reeloop.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetos")
data class Objeto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val conservacion: String,
    val peso: Double,
    val modalidad: String,                 // Donación, Préstamo o Intercambio
    val estado: String = Opciones.DISPONIBLE,
    val solicitante: String = "",          // quién lo pidió
    val fechaPublicado: Long = System.currentTimeMillis(),
    val fechaSolicitado: Long = 0L,
    val fechaEntregado: Long = 0L
)