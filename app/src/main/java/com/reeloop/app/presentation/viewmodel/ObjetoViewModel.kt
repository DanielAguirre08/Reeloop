package com.reeloop.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reeloop.app.data.AppDatabase
import com.reeloop.app.data.ObjetoRepositoryImpl
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.domain.repository.ObjetoRepository
import com.reeloop.app.presentation.event.ObjetoEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//Simulaciom
private val solicitantes = listOf("Carlos Mendoza", "Lucía Herrera", "Diego Ramos")
class ObjetoViewModel(application: Application) : AndroidViewModel(application){
    private val repository: ObjetoRepository =
        ObjetoRepositoryImpl(AppDatabase.getDatabase(application).objetoDao())
    private val _state = MutableStateFlow(ObjetoState())

    val state: StateFlow<ObjetoState> = combine(_state, repository.getObjetos()) { estado, lista ->
        estado.copy(listaObjeto = lista)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ObjetoState())

    init {
        cargarDatosDeMuestra()
    }

    fun onEvent(event: ObjetoEvent) {
        when (event) {
            is ObjetoEvent.OnExitoVisto -> _state.update { it.copy(exito = false) }
            is ObjetoEvent.OnNombreChange -> _state.update { it.copy(nombre = event.valor, error = "") }
            is ObjetoEvent.OnDescripcionChange -> _state.update { it.copy(descripcion = event.valor, error = "") }
            is ObjetoEvent.OnCategoriaChange -> _state.update { it.copy(categoria = event.valor) }
            is ObjetoEvent.OnConservacionChange -> _state.update { it.copy(conservacion = event.valor) }
            is ObjetoEvent.OnPesoChange -> _state.update { it.copy(peso = event.valor, error = "") }
            is ObjetoEvent.OnModalidadChange -> _state.update { it.copy(modalidad = event.valor) }
            is ObjetoEvent.OnBusquedaChange -> _state.update { it.copy(busqueda = event.valor) }
            is ObjetoEvent.OnFiltroModalidad -> _state.update { it.copy(filtroModalidad = event.valor) }
            is ObjetoEvent.OnSolicitar -> solicitar(event.id)
            is ObjetoEvent.OnAceptar -> cambiarEstado(event.id) { it.copy(estado = Opciones.ACEPTADO) }
            is ObjetoEvent.OnRechazar -> cambiarEstado(event.id) {
                it.copy(estado = Opciones.DISPONIBLE, solicitante = "", fechaSolicitado = 0L)
            }
            ObjetoEvent.OnPublicar -> publicar()
            ObjetoEvent.OnPublicadoConsumido -> _state.update { it.copy(publicado = false) }
        }
    }

    private fun publicar() {
        val s = _state.value
        val peso = s.peso.replace(',', '.').toDoubleOrNull()

        val error = when {
            s.nombre.isBlank() -> "Escribe el nombre del objeto"
            s.descripcion.isBlank() -> "Escribe una descripción"
            peso == null || peso <= 0 -> "Ingresa un peso válido"
            else -> ""
        }
        if (error.isNotEmpty()) {
            _state.update { it.copy(error = error) }
            return
        }


        viewModelScope.launch {
            repository.insertar(
                Objeto(
                    nombre = s.nombre.trim(),
                    descripcion = s.descripcion.trim(),
                    categoria = s.categoria,
                    conservacion = s.conservacion,
                    peso = peso ?: 0.0,
                    modalidad = s.modalidad
                )
            )
            _state.update { ObjetoState().copy(publicado = true, exito = true) }        }
    }
    // Busca el objeto, le aplica el cambio y lo guarda
    private fun cambiarEstado(id: Int, cambio: (Objeto) -> Objeto) {
        val objeto = state.value.listaObjeto.find { it.id == id } ?: return
        viewModelScope.launch { repository.actualizar(cambio(objeto)) }
    }
    private fun solicitar(id: Int) {
        val objeto = state.value.listaObjeto.find { it.id == id } ?: return
        if (objeto.estado != Opciones.DISPONIBLE) return

        viewModelScope.launch {
            repository.actualizar(
                objeto.copy(
                    estado = Opciones.SOLICITADO,
                    solicitante = solicitantes[id % solicitantes.size],
                    fechaSolicitado = System.currentTimeMillis()
                )
            )
        }
    }

    private fun cargarDatosDeMuestra() {
        viewModelScope.launch {
            if (repository.contar() > 0) return@launch

            val ahora = System.currentTimeMillis()
            val hora = 60 * 60 * 1000L
            val dia = 24 * hora
            val objetos = listOf(
                Objeto(
                    nombre = "Libro de cálculo",
                    descripcion = "Texto universitario con ejercicios resueltos y anotaciones útiles.",
                    categoria = "Académico",
                    conservacion = "Buen estado",
                    peso = 0.7,
                    modalidad = Opciones.DONACION,
                    fechaPublicado = ahora - 6 * dia
                ),
                Objeto(
                    nombre = "Novela juvenil",
                    descripcion = "Novela en buen estado, ideal para compartir durante el ciclo.",
                    categoria = "Libros",
                    conservacion = "Buen estado",
                    peso = 0.4,
                    modalidad = Opciones.PRESTAMO,
                    fechaPublicado = ahora - 4 * dia
                ),
                Objeto(
                    nombre = "Regla T y escuadras",
                    descripcion = "Juego de dibujo técnico completo y listo para reutilizar.",
                    categoria = "Académico",
                    conservacion = "Como nuevo",
                    peso = 0.3,
                    modalidad = Opciones.INTERCAMBIO,
                    estado = Opciones.ACEPTADO,
                    solicitante = "Diego Ramos",
                    fechaPublicado = ahora - 8 * dia,
                    fechaSolicitado = ahora - 2 * hora
                ),
                Objeto(
                    nombre = "Mochila universitaria",
                    descripcion = "Mochila amplia, limpia y con todos los cierres operativos.",
                    categoria = "Accesorios",
                    conservacion = "Buen estado",
                    peso = 0.8,
                    modalidad = Opciones.DONACION,
                    estado = Opciones.SOLICITADO,
                    solicitante = "Carlos Mendoza",
                    fechaPublicado = ahora - 3 * dia,
                    fechaSolicitado = ahora - 25 * 60 * 1000L
                ),
                Objeto(
                    nombre = "Calculadora científica",
                    descripcion = "Calculadora funcional con tapa protectora.",
                    categoria = "Tecnología",
                    conservacion = "Usado",
                    peso = 0.2,
                    modalidad = Opciones.DONACION,
                    estado = Opciones.ENTREGADO,
                    solicitante = "Lucía Herrera",
                    fechaPublicado = ahora - 14 * dia,
                    fechaSolicitado = ahora - 10 * dia,
                    fechaEntregado = ahora - 7 * dia
                )
            )
            objetos.forEach { repository.insertar(it) }
        }
    }
}
