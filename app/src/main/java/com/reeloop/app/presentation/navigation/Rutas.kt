package com.reeloop.app.navigation

//cada valor es una ruta de la app l, que redirigue a una pantalla

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val EXPLORAR = "explorar"
    const val PUBLICAR = "publicar"
    const val IMPACTO = "impacto"
    const val PERFIL = "perfil"
    const val MIS_OBJETOS = "mis_objetos"
    const val SOLICITUDES = "solicitudes"
    const val DETALLE = "detalle/{id}"
    const val CODIGO_QR = "codigo_qr/{id}"
    const val PASAPORTE = "pasaporte/{id}"
    const val ESCANEAR_QR = "escanear_qr?id={id}"
    fun detalle(id: String) = "detalle/$id"
    fun codigoQr(id: String) = "codigo_qr/$id"
    fun pasaporte(id: String) = "pasaporte/$id"
    fun escanearQr(id: String? = null) =
        if (id != null) "escanear_qr?id=$id" else "escanear_qr"

    val sinBarras = listOf(LOGIN, REGISTRO)

    val sinBarraInferior = listOf(CODIGO_QR, ESCANEAR_QR)

    val conFlechaAtras = listOf(DETALLE, MIS_OBJETOS, SOLICITUDES, CODIGO_QR, ESCANEAR_QR, PASAPORTE)

    fun getTitulo(ruta: String?): String = when (ruta) {
        INICIO -> "ReLoop"
        EXPLORAR -> "Explorar"
        PUBLICAR -> "Publicar objeto"
        IMPACTO -> "EcoImpacto"
        PERFIL -> "Perfil"
        MIS_OBJETOS -> "Mis objetos"
        SOLICITUDES -> "Solicitudes"
        DETALLE -> "Detalle del objeto"
        CODIGO_QR -> "Código QR"
        ESCANEAR_QR -> "Escanear QR"
        PASAPORTE -> "Pasaporte Circular"
        else -> "ReLoop"
    }

    fun pestanaDe(ruta: String?): String? = when (ruta) {
        DETALLE -> EXPLORAR
        MIS_OBJETOS -> PUBLICAR
        PASAPORTE -> IMPACTO
        SOLICITUDES -> PERFIL
        else -> ruta
    }
}
