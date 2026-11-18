package com.reeloop.app.presentation.event

interface UiEvent {
    data class MostrarSnackbar(val mensaje: String, val tipo: SnackbarTipo) : UiEvent
}