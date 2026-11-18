package com.reeloop.app.data.fake

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.domain.model.Usuario

// Datos de prueba para trabajar SIN la nube. Las dos cuentas usan la clave 123456.
internal object DatosFake {
    const val CLAVE_DEMO = "123456"
    const val RETARDO_MS = 400L   // simula la espera de la red para probar "Cargando..."

    val ana = Usuario(id = "u-ana", nombre = "Ana Torres", correo = "ana@correo.com")
    val carlos = Usuario(id = "u-carlos", nombre = "Carlos Mendoza", correo = "carlos@correo.com")

    fun objetosIniciales(ahora: Long): List<Objeto> {
        val hora = 60 * 60 * 1000L
        val dia = 24 * hora
        return listOf(
            Objeto(
                id = "o-libro", nombre = "Libro de cálculo",
                descripcion = "Texto universitario con ejercicios resueltos y anotaciones útiles.",
                categoria = "Académico", conservacion = "Buen estado", peso = 0.7,
                modalidad = Opciones.DONACION,
                propietarioId = ana.id, propietarioNombre = ana.nombre,
                fechaPublicado = ahora - 6 * dia
            ),
            Objeto(
                id = "o-mochila", nombre = "Mochila universitaria",
                descripcion = "Mochila amplia, limpia y con todos los cierres operativos.",
                categoria = "Accesorios", conservacion = "Buen estado", peso = 0.8,
                modalidad = Opciones.DONACION, estado = Opciones.SOLICITADO,
                propietarioId = ana.id, propietarioNombre = ana.nombre,
                solicitanteId = carlos.id, solicitanteNombre = carlos.nombre,
                fechaPublicado = ahora - 3 * dia, fechaSolicitado = ahora - 25 * 60 * 1000L
            ),
            Objeto(
                id = "o-regla", nombre = "Regla T y escuadras",
                descripcion = "Juego de dibujo técnico completo y listo para reutilizar.",
                categoria = "Académico", conservacion = "Como nuevo", peso = 0.3,
                modalidad = Opciones.INTERCAMBIO, estado = Opciones.ACEPTADO,
                propietarioId = ana.id, propietarioNombre = ana.nombre,
                solicitanteId = carlos.id, solicitanteNombre = carlos.nombre,
                codigoEntrega = "482913",
                fechaPublicado = ahora - 8 * dia, fechaSolicitado = ahora - 2 * hora
            ),
            Objeto(
                id = "o-novela", nombre = "Novela juvenil",
                descripcion = "Novela en buen estado, ideal para compartir durante el ciclo.",
                categoria = "Libros", conservacion = "Buen estado", peso = 0.4,
                modalidad = Opciones.PRESTAMO,
                propietarioId = carlos.id, propietarioNombre = carlos.nombre,
                fechaPublicado = ahora - 4 * dia
            ),
            Objeto(
                id = "o-audifonos", nombre = "Audífonos de diadema",
                descripcion = "Funcionan bien, con almohadillas nuevas.",
                categoria = "Tecnología", conservacion = "Buen estado", peso = 0.25,
                modalidad = Opciones.PRESTAMO, estado = Opciones.ACEPTADO,
                propietarioId = carlos.id, propietarioNombre = carlos.nombre,
                solicitanteId = ana.id, solicitanteNombre = ana.nombre,
                codigoEntrega = "731550",
                fechaPublicado = ahora - 5 * dia, fechaSolicitado = ahora - 1 * dia
            ),
            Objeto(
                id = "o-calculadora", nombre = "Calculadora científica",
                descripcion = "Calculadora funcional con tapa protectora.",
                categoria = "Tecnología", conservacion = "Usado", peso = 0.2,
                modalidad = Opciones.DONACION, estado = Opciones.ENTREGADO,
                propietarioId = carlos.id, propietarioNombre = carlos.nombre,
                solicitanteId = ana.id, solicitanteNombre = ana.nombre,
                fechaPublicado = ahora - 14 * dia, fechaSolicitado = ahora - 10 * dia,
                fechaEntregado = ahora - 7 * dia
            )
        )
    }
}
