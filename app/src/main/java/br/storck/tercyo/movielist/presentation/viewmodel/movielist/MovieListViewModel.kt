package br.storck.tercyo.movielist.presentation.viewmodel.movielist

import android.view.View
import androidx.lifecycle.*
import br.storck.tercyo.movielist.R
import br.storck.tercyo.movielist.infrastructure.repository.GenreRepository
import br.storck.tercyo.movielist.infrastructure.repository.MovieRepository
import br.storck.tercyo.movielist.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieListViewModel(
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {
    private val _loadingPanelVisibility = MutableLiveData<Int>().apply { this.value = View.VISIBLE }
    private val _sortedByDescending = MutableLiveData<Boolean>().apply { this.value = false }
    private val _movies = MutableLiveData<MutableList<Movie>?>()

    val selectedMenuItem = MutableLiveData<Int>().apply { this.value = R.id.menu_home }
    val loadingPanelVisibility: LiveData<Int> get() = this._loadingPanelVisibility
    val sortedByDescending: LiveData<Boolean> get() = this._sortedByDescending
    val movies: LiveData<MutableList<Movie>?> get() = this._movies
    val selectedMovie = MutableLiveData<Movie?>()
    val fabIcon: LiveData<Int> = Transformations.map(this.sortedByDescending) {
        if (it) {
            R.drawable.sort_descending
        } else {
            R.drawable.sort_ascending
        }
    }

    private fun order(movies: MutableList<Movie>?): MutableList<Movie>? {
        if (this._sortedByDescending.value == false) {
            movies?.sortBy { movie ->
                movie.releaseDate
            }

            return movies
        }

        movies?.sortByDescending { movie ->
            movie.releaseDate
        }

        return movies
    }

    fun sortByReleaseDate() {
        this._sortedByDescending.value = this._sortedByDescending.value?.let { !it } ?: run { true }
    }

    fun manageFavorite(movie: Movie) {
        movie.favorite = movie.favorite?.let { !it } ?: run { true }

        GlobalScope.launch(this@MovieListViewModel.dispatcher) {
            if (movie.favorite == true) {
                this@MovieListViewModel.movieRepository.addFavorite(movie)
            } else {
                this@MovieListViewModel.movieRepository.removeFavorite(movie)
            }
        }
    }

    fun loadMoreMovies() {
        if (this.selectedMenuItem.value != R.id.menu_home || !this.movieRepository.canLoad) {
            return
        }

        this._loadingPanelVisibility.postValue(View.VISIBLE)

        GlobalScope.launch(this@MovieListViewModel.dispatcher) {
            val genres = this@MovieListViewModel.genreRepository.genres
            val movies = movieRepository.fetchMoreMovies(genres)
            this@MovieListViewModel._movies.postValue(movies)
        }
    }

    init {
        this.selectedMenuItem.observeForever {
            when(it) {
                R.id.menu_home -> {
                    GlobalScope.launch(this@MovieListViewModel.dispatcher) {
                        val genres = genreRepository.genres
                        val movies = movieRepository.movies(genres)
                        this@MovieListViewModel.order(movies)
                        this@MovieListViewModel._movies.postValue(movies)
                    }
                }
                R.id.menu_favorites -> {
                    GlobalScope.launch(this@MovieListViewModel.dispatcher) {
                        val genres = genreRepository.genres
                        val favorites = movieRepository.favorites(genres)
                        this@MovieListViewModel.order(favorites)
                        this@MovieListViewModel._movies.postValue(favorites)
                    }
                }
            }
        }
        this._movies.observeForever {
            this._loadingPanelVisibility.value = View.GONE
        }
        this._sortedByDescending.observeForever {
            this.order(this._movies.value)
        }
    }
}