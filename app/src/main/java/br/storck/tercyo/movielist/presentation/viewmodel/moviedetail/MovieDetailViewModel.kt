package br.storck.tercyo.movielist.presentation.viewmodel.moviedetail

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.storck.tercyo.movielist.model.Movie


class MovieDetailViewModel(
    private val movie: Movie?
): ViewModel() {
    val title = MutableLiveData<String>().apply {
        this.value = movie?.title
    }
    val poster = MutableLiveData<Bitmap>().apply {
        this.value = movie?.poster
    }
    val releaseDate = MutableLiveData<String>().apply {
        this.value = movie?.releaseDateFormatted
    }
    val genres = MutableLiveData<List<String>>().apply {
        this.value = movie?.genres?.let { genres ->
            genres.map { genre -> genre.name ?: "" }
        }
    }
    val overview = MutableLiveData<String>().apply {
        this.value = movie?.overview
    }
}