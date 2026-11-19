package com.reeloop.app.presentation.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiEventBus @Inject constructor(){
    private val _eventos= MutableSharedFlow<UiEvent>()
    val eventos =_eventos.asSharedFlow()
    suspend fun lanzarEvento(evento: UiEvent) {
        _eventos.emit(evento)
    }
}