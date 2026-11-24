package com.example.mpaz_real_real.data.model

data class Pokemon(
    val id: Int,
    val name: String,
    val sprites: ImagenesPokemon? = null
)

data class ImagenesPokemon(
    val front_default: String?
)