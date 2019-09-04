package br.storck.tercyo.movielist.presentation.viewmodel.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.TheMovieService
import br.storck.tercyo.movielist.infrastructure.dal.ApplicationDatabase
import br.storck.tercyo.movielist.infrastructure.repository.GenreRepository
import br.storck.tercyo.movielist.infrastructure.repository.MovieRepository
import br.storck.tercyo.movielist.infrastructure.resources.DownloadImage
import br.storck.tercyo.movielist.infrastructure.resources.FileManager

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val service = TheMovieService(dateFormat = "yyyy-MM-dd")

        return MovieListViewModel(
            GenreRepository(
                service = service,
                genreDao = ApplicationDatabase.instance?.genreDao()
            ),
            MovieRepository(
                service = service,
                movieDao = ApplicationDatabase.instance?.movieDao(),
                downloadImage = DownloadImage(),
                fileManager = FileManager()
            )
        ) as T
    }
}