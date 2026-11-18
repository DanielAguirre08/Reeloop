package com.reeloop.app.domain.model

// Números que muestran Inicio, EcoImpacto y Perfil. Los calcula CalcularImpactoUseCase.
data class ResumenImpacto(
    val reutilizados: Int = 0,          // ciclos completados en los que participé
    val donaciones: Int = 0,
    val prestamos: Int = 0,
    val intercambios: Int = 0,
    val kgEvitados: Double = 0.0,
    val publicados: Int = 0,            // objetos que publiqué
    val solicitudesPendientes: Int = 0  // solicitudes recibidas sin responder
)
