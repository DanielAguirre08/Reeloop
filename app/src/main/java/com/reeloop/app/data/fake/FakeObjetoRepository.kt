package com.reeloop.app.data.fake

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.domain.model.Usuario
import com.reeloop.app.domain.repository.ObjetoRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

// Reemplazo en memoria de la nube. Aplica las MISMAS reglas y mensajes que la
// Lambda reloop_objetos, para que las pantallas se comporten igual con o sin nube.
// Los datos se pierden al cerrar la app: es solo para desarrollar.
@Singleton
class FakeObjetoRepository @Inject constructor() : ObjetoRepository {

    private val objetos = DatosFake.objetosIniciales(System.currentTimeMillis()).toMutableList()
    private var contador = 0

    override suspend fun getObjetos(usuarioId: String): Result<List<Objeto>> {
        delay(DatosFake.RETARDO_MS)
        return Result.success(
            objetos.sortedByDescending { it.fechaPublicado }.map { it.visiblePara(usuarioId) }
        )
    }

    override suspend fun getObjetoById(id: String, usuarioId: String): Result<Objeto> {
        delay(DatosFake.RETARDO_MS)
        val objeto = buscar(id).getOrElse { return Result.failure(it) }
        return Result.success(objeto.visiblePara(usuarioId))
    }

    override suspend fun publicar(objeto: Objeto): Result<Objeto> {
        delay(DatosFake.RETARDO_MS)
        contador++
        val nuevo = objeto.copy(
            id = "o-nuevo-$contador",
            estado = Opciones.DISPONIBLE,
            fechaPublicado = System.currentTimeMillis()
        )
        objetos.add(nuevo)
        return Result.success(nuevo)
    }

    override suspend fun eliminar(id: String, usuarioId: String): Result<Unit> {
        delay(DatosFake.RETARDO_MS)
        val objeto = buscar(id).getOrElse { return Result.failure(it) }
        if (objeto.propietarioId != usuarioId) return falla("Solo el propietario puede eliminar el objeto")
        if (objeto.estado != Opciones.DISPONIBLE) return falla("No puedes eliminar un objeto con una solicitud en curso")
        objetos.removeAll { it.id == id }
        return Result.success(Unit)
    }

    override suspend fun solicitar(id: String, solicitante: Usuario): Result<Objeto> {
        delay(DatosFake.RETARDO_MS)
        val objeto = buscar(id).getOrElse { return Result.failure(it) }
        if (objeto.propietarioId == solicitante.id) return falla("No puedes solicitar tu propio objeto")
        if (objeto.estado != Opciones.DISPONIBLE) return falla("El objeto ya no está disponible")
        return guardar(
            objeto.copy(
                estado = Opciones.SOLICITADO,
                solicitanteId = solicitante.id,
                solicitanteNombre = solicitante.nombre,
                fechaSolicitado = System.currentTimeMillis()
            ),
            solicitante.id
        )
    }

    override suspend fun aceptar(id: String, usuarioId: String): Result<Objeto> {
        delay(DatosFake.RETARDO_MS)
        val objeto = buscar(id).getOrElse { return Result.failure(it) }
        if (objeto.propietarioId != usuarioId) return falla("Solo el propietario puede responder la solicitud")
        if (objeto.estado != Opciones.SOLICITADO) return falla("La solicitud ya fue respondida")
        val codigo = (100000..999999).random().toString()
        return guardar(objeto.copy(estado = Opciones.ACEPTADO, codigoEntrega = codigo), usuarioId)
    }

    override suspend fun rechazar(id: String, usuarioId: String): Result<Objeto> {
        delay(DatosFake.RETARDO_MS)
        val objeto = buscar(id).getOrElse { return Result.failure(it) }
        if (objeto.propietarioId != usuarioId) return falla("Solo el propietario puede responder la solicitud")
        if (objeto.estado != Opciones.SOLICITADO) return falla("La solicitud ya fue respondida")
        return guardar(
            objeto.copy(
                estado = Opciones.DISPONIBLE,
                solicitanteId = "",
                solicitanteNombre = "",
                fechaSolicitado = 0L
            ),
            usuarioId
        )
    }

    override suspend fun entregar(id: String, codigo: String, usuarioId: String): Result<Objeto> {
        delay(DatosFake.RETARDO_MS)
        val objeto = buscar(id).getOrElse { return Result.failure(it) }
        if (objeto.solicitanteId != usuarioId) return falla("Solo quien solicitó el objeto puede confirmar la entrega")
        if (objeto.estado != Opciones.ACEPTADO) return falla("Este objeto no tiene una entrega pendiente")
        if (objeto.codigoEntrega != codigo) return falla("Código incorrecto")
        return guardar(
            objeto.copy(estado = Opciones.ENTREGADO, fechaEntregado = System.currentTimeMillis()),
            usuarioId
        )
    }

    private fun buscar(id: String): Result<Objeto> =
        objetos.find { it.id == id }?.let { Result.success(it) }
            ?: Result.failure(Exception("El objeto no existe"))

    private fun guardar(objeto: Objeto, usuarioId: String): Result<Objeto> {
        val posicion = objetos.indexOfFirst { it.id == objeto.id }
        objetos[posicion] = objeto
        return Result.success(objeto.visiblePara(usuarioId))
    }

    private fun <T> falla(mensaje: String): Result<T> = Result.failure(Exception(mensaje))

    // El código de entrega solo lo ve el propietario (él muestra el QR).
    private fun Objeto.visiblePara(usuarioId: String): Objeto =
        if (propietarioId == usuarioId) this else copy(codigoEntrega = "")
}
