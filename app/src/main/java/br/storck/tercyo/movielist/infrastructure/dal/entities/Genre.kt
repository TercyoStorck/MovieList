package br.storck.tercyo.movielist.infrastructure.dal.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre(
    @PrimaryKey
    val id: Int,
    val name: String? = null
): DatabaseEntity<br.storck.tercyo.movielist.model.Genre> {
    override fun cast(): br.storck.tercyo.movielist.model.Genre {
        return br.storck.tercyo.movielist.model.Genre(
            id = this.id,
            name = this.name
        )
    }

    companion object {
        fun fromGenre(genre: br.storck.tercyo.movielist.model.Genre): Genre? {
            return genre.id?.let { id ->
                Genre(
                    id = id,
                    name = genre.name
                )
            }
        }
    }
}