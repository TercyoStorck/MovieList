package br.storck.tercyo.movielist.infrastructure.repository

import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.TheMovieService
import br.storck.tercyo.movielist.infrastructure.dal.dao.MovieDAO
import br.storck.tercyo.movielist.infrastructure.resources.DownloadImage
import br.storck.tercyo.movielist.infrastructure.resources.FileManager
import br.storck.tercyo.movielist.model.Genre
import br.storck.tercyo.movielist.model.Movie
import java.io.IOException

class MovieRepository(
    private val service: TheMovieService,
    private val movieDao: MovieDAO?,
    private val downloadImage: DownloadImage? = null,
    private val fileManager: FileManager? = null
) {
    private var movies: MutableList<Movie>? = null
    private var favorites: MutableList<Movie>? = null
    private var pages: Int = 0
    private var currentPage = 0

    val canLoad: Boolean get() = this.currentPage > 0 && this.currentPage < this.pages

    private fun fetchMovies(genres: List<Genre>?): MutableList<Movie>? {
        try {
            return service.endpoints.movies(this.currentPage).execute().body()?.let { resultsDto ->
                resultsDto.totalPages?.let { this.pages = it }
                resultsDto.results?.map { dto ->
                    Movie(
                        id = dto.id,
                        title = dto.title,
                        poster = this.downloadImage?.downloadImage("https://image.tmdb.org/t/p/w185/${dto.posterPath}"),
                        releaseDate = dto.releaseDate,
                        genres = dto.genreIds?.map { id ->
                            genres?.filter { genre ->
                                genre.id == id
                            }
                        }?.toList()?.flatMap { it!! },
                        overview = dto.overview
                    )
                }
            }?.toMutableList()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun movies(genres: List<Genre>?): MutableList<Movie>? {
        return this.movies?.let {
            it
        } ?: run {
            this.currentPage++
            this.movies = fetchMovies(genres)

            val favorites = this.favorites(genres)

            favorites?.let {
                this.movies?.forEach { movie ->
                    movie.favorite = it.any { movie.id == it.id }
                }
            }

            this.movies
        }
    }

    fun favorites(genres: List<Genre>?): MutableList<Movie>? {
        return this.favorites?.let {
            it
        } ?: run {
            this.favorites = movieDao?.getAll()?.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    releaseDate = entity.releaseDate,
                    poster = fileManager?.loadImageBitmap(entity.posterPath),
                    genres = entity.genreIds?.map { id ->
                        genres?.filter { genre ->
                            genre.id == id
                        }
                    }?.toList()?.flatMap { it!! },
                    overview = entity.overview,
                    favorite = true
                )
            }?.toMutableList()

            this.favorites
        }
    }

    fun addFavorite(movie: Movie) {
        movieDao?.toEntity(movie)?.let {
            fileManager?.saveImage(movie.poster, "${movie.id}.jpeg")
            movieDao.add(it)
            this.favorites?.add(movie)
        }
    }

    fun removeFavorite(movie: Movie) {
        movieDao?.toEntity(movie)?.let {
            movieDao.delete(it)
            this.favorites?.remove(movie)
        }
    }

    fun fetchMoreMovies(genres: List<Genre>?): MutableList<Movie>? {
        this.currentPage++
        val movies = fetchMovies(genres)

        val favorites = this.favorites(genres)

        favorites?.let {
            movies?.forEach { movie ->
                movie.favorite = it.any { movie.id == it.id }
            }
        }

        movies?.let {
            this.movies?.addAll(it)
        }

        return this.movies
    }
}