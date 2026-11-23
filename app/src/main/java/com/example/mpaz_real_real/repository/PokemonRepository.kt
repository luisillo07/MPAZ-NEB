package com.example.mpaz_real_real.repository

import com.example.mpaz_real_real.data.model.Pokemon
import com.example.mpaz_real_real.data.remote.PokeApiInstance

open class PokemonRepository {
    open suspend fun obtenerPokemon(id: Int): Pokemon {
        return PokeApiInstance.api.obtenerPokemon(id)
    }
}