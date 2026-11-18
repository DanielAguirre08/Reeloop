package com.reeloop.app.domain.usecase

import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.domain.model.ResumenImpacto
import javax.inject.Inject

// Cálculo puro: no llama a la red. Recibe la lista que ya cargó GetObjetosUseCase.
class CalcularImpactoUseCase @Inject constructor() {
    operator fun invoke(objetos: List<Objeto>, usuarioId: String): ResumenImpacto {
        val mios = objetos.filter { it.propietarioId == usuarioId }
        val ciclos = objetos.filter {
            it.estado == Opciones.ENTREGADO &&
                (it.propietarioId == usuarioId || it.solicitanteId == usuarioId)
        }
        return ResumenImpacto(
            reutilizados = ciclos.size,
            donaciones = ciclos.count { it.modalidad == Opciones.DONACION },
            prestamos = ciclos.count { it.modalidad == Opciones.PRESTAMO },
            intercambios = ciclos.count { it.modalidad == Opciones.INTERCAMBIO },
            kgEvitados = ciclos.sumOf { it.peso },
            publicados = mios.size,
            solicitudesPendientes = mios.count { it.estado == Opciones.SOLICITADO }
        )
    }
}
