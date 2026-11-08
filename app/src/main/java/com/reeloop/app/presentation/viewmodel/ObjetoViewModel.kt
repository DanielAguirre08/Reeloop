package com.reeloop.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reeloop.app.data.AppDatabase
import com.reeloop.app.data.ObjetoRepositoryImpl
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.repository.ObjetoRepository
import com.reeloop.app.presentation.event.ObjetoEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ObjetoViewModel(application: Application) : AndroidViewModel(application){
    private val repository: ObjetoRepository =
        ObjetoRepositoryImpl(AppDatabase.getDatabase(application).objetoDao())
    private val _state = MutableStateFlow(ObjetoState())

    val state: StateFlow<ObjetoState> = combine(_state, repository.getObjetos()) { estado, lista ->
        estado.copy(listaObjeto = lista)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ObjetoState())

    fun onEvent(event: ObjetoEvent) {
        when (event) {
            is ObjetoEvent.OnExitoVisto -> _state.update { it.copy(exito = false) }
            is ObjetoEvent.OnNombreChange -> _state.update { it.copy(nombre = event.valor, error = "") }
            is ObjetoEvent.OnDescripcionChange -> _state.update { it.copy(descripcion = event.valor, error = "") }
            is ObjetoEvent.OnCategoriaChange -> _state.update { it.copy(categoria = event.valor) }
            is ObjetoEvent.OnConservacionChange -> _state.update { it.copy(conservacion = event.valor) }
            is ObjetoEvent.OnPesoChange -> _state.update { it.copy(peso = event.valor, error = "") }
            is ObjetoEvent.OnModalidadChange -> _state.update { it.copy(modalidad = event.valor) }
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
}