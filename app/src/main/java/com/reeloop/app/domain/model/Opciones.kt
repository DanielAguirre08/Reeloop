package com.reeloop.app.domain.model

object Opciones {
    const val DISPONIBLE = "Disponible"
    const val SOLICITADO = "Solicitado"
    const val ACEPTADO = "Aceptado"
    const val ENTREGADO = "Entregado"

    const val DONACION = "Donación"
    const val PRESTAMO = "Préstamo"
    const val INTERCAMBIO = "Intercambio"

    val modalidades = listOf(DONACION, PRESTAMO, INTERCAMBIO)
    val categorias = listOf("Académico", "Libros", "Accesorios", "Hogar", "Tecnología", "Ropa", "Otros")
    val conservaciones = listOf("Como nuevo", "Buen estado", "Usado")
}