package com.reeloop.app.util

// Formato del texto que va dentro del QR de entrega: reloop:<idObjeto>:<codigo>
object CodigoQr {
    private const val PREFIJO = "reloop"

    fun crear(objetoId: String, codigo: String): String = "$PREFIJO:$objetoId:$codigo"

    // Devuelve (idObjeto, codigo) o null si el QR no es de ReLoop.
    fun leer(texto: String): Pair<String, String>? {
        val partes = texto.trim().split(":")
        if (partes.size != 3 || partes[0] != PREFIJO) return null
        if (partes[1].isBlank() || partes[2].isBlank()) return null
        return partes[1] to partes[2]
    }
}
