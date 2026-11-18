package com.reeloop.app.presentation.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UIeventBus @Inject constructor(){
    private val _eventos= MutableSharedFlow<UiEvent>()
    val eventoss =_eventos.asSharedFlow()
    suspend fun lanzarEvento(event: UiEvent){
        _eventos.emit(evento)
    }
}