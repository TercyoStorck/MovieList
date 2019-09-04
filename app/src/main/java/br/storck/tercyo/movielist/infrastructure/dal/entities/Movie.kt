package br.storck.tercyo.movielist.infrastructure.dal.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String? = null,
    val posterPath: String? = null,
    val releaseDate: Date? = null,
    val genreIds: List<Int>? = null,
    val overview: String? = null
)