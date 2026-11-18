package com.reeloop.app.util

object Validacion {
    const val CLAVE_MINIMA = 6
    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun correoValido(correo: String): Boolean = formatoCorreo.matches(correo.trim())
    fun claveValida(clave: String): Boolean = clave.length >= CLAVE_MINIMA

    // Acepta "0,8" y "0.8". Devuelve null si no es un número.
    fun pesoDesdeTexto(texto: String): Double? = texto.trim().replace(',', '.').toDoubleOrNull()
}
