package br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto

data class GenreDTO(
    var genres: List<Value>? = null
) {
    inner class Value(
        var id: Int? = null,
        var name: String? = null
    )
}