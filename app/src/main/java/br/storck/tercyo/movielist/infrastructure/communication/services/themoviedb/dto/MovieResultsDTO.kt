package br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto

import com.google.gson.annotations.SerializedName

data class MovieResultsDTO(
    val results: List<MovieDTO>? = null,
    val page: Int? = null,
    @SerializedName(value = "total_results")
    val totalResults: Int? = null,
    val dates: MoviesReleaseDatesDTO? = null,
    @SerializedName(value = "total_pages")
    val totalPages: Int? = null
)