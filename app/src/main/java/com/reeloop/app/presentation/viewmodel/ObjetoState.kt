package com.reeloop.app.presentation.viewmodel

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones

data class ObjetoState(
    val listaObjeto: List<Objeto> = emptyList(),
    val exito: Boolean = false,
    val nombre : String ="",
    val descripcion: String = "",
    val categoria: String = Opciones.categorias.first(),
    val conservacion: String = Opciones.conservaciones[1],
    val peso: String = "",
    val modalidad: String = Opciones.DONACION,
    val error: String = "",
    val publicado: Boolean = false,
    val busqueda: String = "",
    val filtroModalidad: String = "Todo")
