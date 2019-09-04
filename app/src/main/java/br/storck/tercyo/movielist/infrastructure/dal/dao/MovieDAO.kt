package br.storck.tercyo.movielist.infrastructure.dal.dao

import androidx.room.*
import br.storck.tercyo.movielist.infrastructure.dal.entities.Movie

@Dao
abstract class MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun add(movie: Movie)

    @Update
    abstract fun update(movie: Movie)

    @Delete
    abstract fun delete(movie: Movie)

    @Query("SELECT * FROM Movie")
    abstract fun getAll(): List<Movie>?

    fun toEntity(movie: br.storck.tercyo.movielist.model.Movie): Movie? {
        return movie.id?.let {id ->
            Movie(
                id = id,
                title = movie.title,
                posterPath = "${movie.id}.jpeg",
                releaseDate = movie.releaseDate,
                genreIds = movie.genres?.map { it.id ?: 0 },
                overview = movie.overview
            )
        }
    }
}