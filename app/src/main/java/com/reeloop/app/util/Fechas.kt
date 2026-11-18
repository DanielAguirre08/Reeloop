package com.reeloop.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Fechas {

    // Convierte una fecha guardada (milisegundos) en "hace 5 min", "hace 2 h"...
    fun haceCuanto(tiempo: Long): String {
        val minutos = (System.currentTimeMillis() - tiempo) / 60_000
        return when {
            minutos < 1 -> "hace un momento"
            minutos < 60 -> "hace $minutos min"
            minutos < 1440 -> "hace ${minutos / 60} h"
            else -> "hace ${minutos / 1440} días"
        }
    }

    // 1795000000000 -> "18 nov 2026 · 21:14". Devuelve "Pendiente" si la fecha es 0.
    fun formatear(tiempo: Long): String {
        if (tiempo <= 0L) return "Pendiente"
        return SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.forLanguageTag("es-PE")).format(Date(tiempo))
    }
}
