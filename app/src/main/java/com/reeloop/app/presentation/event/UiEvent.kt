package com.reeloop.app.presentation.event

import com.reeloop.app.presentation.common.SnackbarTipo

interface UiEvent {
    data class MostrarSnackbar(val mensaje: String, val tipo: SnackbarTipo) : UiEvent
}