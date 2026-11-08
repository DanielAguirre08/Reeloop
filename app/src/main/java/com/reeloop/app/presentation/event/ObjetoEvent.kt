package com.reeloop.app.presentation.event
sealed class ObjetoEvent {
    data class OnNombreChange(val valor: String) : ObjetoEvent()
    data class OnDescripcionChange(val valor: String) : ObjetoEvent()
    data class OnCategoriaChange(val valor: String) : ObjetoEvent()
    data class OnConservacionChange(val valor: String) : ObjetoEvent()
    data class OnPesoChange(val valor: String) : ObjetoEvent()
    data class OnModalidadChange(val valor: String) : ObjetoEvent()
    data object OnPublicar : ObjetoEvent()
    data object OnPublicadoConsumido : ObjetoEvent()
    data object OnExitoVisto : ObjetoEvent()
}
