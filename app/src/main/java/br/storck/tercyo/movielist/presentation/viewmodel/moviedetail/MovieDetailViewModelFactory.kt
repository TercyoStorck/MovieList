package br.storck.tercyo.movielist.presentation.viewmodel.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.storck.tercyo.movielist.model.Movie

@Suppress("UNCHECKED_CAST")
class MovieDetailViewModelFactory: ViewModelProvider.Factory {
    companion object {
        var movie: Movie? = null
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val movieParameter = movie?.copy()
        movie = null
        return MovieDetailViewModel(movieParameter) as T
    }
}