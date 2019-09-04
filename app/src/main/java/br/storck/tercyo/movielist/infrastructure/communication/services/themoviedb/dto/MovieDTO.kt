package br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class MovieDTO(
    val id: Int? = null,
    @SerializedName(value = "vote_count")
    val voteCount: Int? = null,
    val video: Boolean? = null,
    @SerializedName(value = "vote_average")
    val voteAverage: Double? = null,
    val title: String? = null,
    val popularity: Double? = null,
    @SerializedName(value = "poster_path")
    val posterPath: String? = null,
    @SerializedName(value = "original_language")
    val originalLanguage: String? = null,
    @SerializedName(value = "original_title")
    val originalTitle: String? = null,
    @SerializedName(value = "genre_ids")
    val genreIds: List<Int>? = null,
    @SerializedName(value = "backdrop_path")
    val backdropPath: String? = null,
    val adult: Boolean? = null,
    val overview: String? = null,
    @SerializedName(value = "release_date")
    val releaseDate: Date? = null
)