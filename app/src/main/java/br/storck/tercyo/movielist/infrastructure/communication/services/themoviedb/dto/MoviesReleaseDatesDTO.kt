package br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto

import java.util.*

data class MoviesReleaseDatesDTO(
    val maximum: Date?,
    val minimum: Date?
)