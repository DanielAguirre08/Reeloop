package com.reeloop.app.util

object Fechas {


    fun haceCuanto(tiempo: Long): String{
        val minutos =(System.currentTimeMillis()-tiempo)/60_000
        return when{
            minutos <1-> "hace un momento"
            minutos < 60 -> "hace $minutos min"
            minutos < 1440 -> "hace ${minutos / 60} h"
            else -> "hace ${minutos / 1440} días"
        }

    }
}