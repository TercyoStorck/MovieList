package br.storck.tercyo.movielist.infrastructure.repository

import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.TheMovieService
import br.storck.tercyo.movielist.infrastructure.dal.dao.GenreDAO
import br.storck.tercyo.movielist.infrastructure.resources.extensions.cast
import br.storck.tercyo.movielist.model.Genre
import java.io.IOException

class GenreRepository(
    private val service: TheMovieService,
    private val genreDao: GenreDAO?
) {
    private var cache: List<Genre>? = null

    val genres: List<Genre>?
        get() {
            return this.cache?.let {
                it
            } ?: run {
                try {
                    val genresFromService = service.endpoints.genre().execute()?.body()?.genres?.map { dto ->
                        Genre(
                            id = dto.id,
                            name = dto.name
                        )
                    }

                    genresFromService?.forEach {
                        br.storck.tercyo.movielist.infrastructure.dal.entities.Genre.fromGenre(it)?.let { entity ->
                            this.genreDao?.add(entity)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                this.cache = this.genreDao?.getAll()?.cast()
                this.cache
            }
        }
}