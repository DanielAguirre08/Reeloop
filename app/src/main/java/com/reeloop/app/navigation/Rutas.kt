package com.reeloop.app.navigation

//cada valor es una ruta de la app l, que redirigue a una pantalla

object Rutas {
    const val LOGIN= "login"
    const val REGISTRO ="registro"
    const val INICIO = "inicio"
    const val EXPLORAR = "explorar"
    const val PUBLICAR = "publicar"
    const val IMPACTO = "impacto"
    const val PERFIL = "perfil"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"
    const val MIS_OBJETOS = "mis_objetos"
    const val SOLICITUDES = "solicitudes"
    const val CODIGO_QR = "codigo_qr/{id}"
    const val ESCANEAR_QR = "escanear_qr/{id}"
    fun codigoQr(id: Int) = "codigo_qr/$id"
    fun escanearQr(id: Int) = "escanear_qr/$id"
    const val PASAPORTE = "pasaporte"

    //son las pantallas donde la no se muestra la barra de navigacion
 val  sinBarra = listOf(LOGIN,REGISTRO,CODIGO_QR,ESCANEAR_QR)

    // las pantallas que son secundarias , iluminan el siguiente intem de barra
    fun pestanaDe(ruta: String?): String? = when(ruta){
        DETALLE -> EXPLORAR
        MIS_OBJETOS -> PUBLICAR
        PASAPORTE -> IMPACTO
        SOLICITUDES ->PERFIL
        else -> ruta

    }
}