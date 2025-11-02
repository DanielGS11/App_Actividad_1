package com.example.app_actividad_1

data class Game(
    //Tuve que cambian a String? para poder asignarle el nombre del intent
    var name: String? = "",
    val score: Int = 0,
    val level: Int = 0
)