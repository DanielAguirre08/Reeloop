package com.reeloop.app.util

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object QrGenerador {
    fun generar(texto: String, tamano: Int = 512): ImageBitmap {
        val matriz = QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, tamano, tamano)
        val oscuro = android.graphics.Color.rgb(36, 90, 59)
        val claro = android.graphics.Color.WHITE
        val pixeles = IntArray(tamano * tamano) { i ->
            if (matriz[i % tamano, i / tamano]) oscuro else claro
        }
        return Bitmap.createBitmap(pixeles, tamano, tamano, Bitmap.Config.ARGB_8888).asImageBitmap()
    }
}