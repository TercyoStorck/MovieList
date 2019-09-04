package br.storck.tercyo.movielist.presentation.viewmodel

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.storck.tercyo.movielist.R
import br.storck.tercyo.movielist.infrastructure.repository.GenreRepository
import br.storck.tercyo.movielist.infrastructure.repository.MovieRepository
import br.storck.tercyo.movielist.model.Genre
import br.storck.tercyo.movielist.model.Movie
import br.storck.tercyo.movielist.presentation.viewmodel.movielist.MovieListViewModel
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.text.SimpleDateFormat

class MovieListViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val genreReposotiryMock = Mockito.mock(GenreRepository::class.java)
    val movieReposotiryMock = Mockito.mock(MovieRepository::class.java)

    @Test
    fun `when start application, selected menu most be home`() = runBlocking {
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        val observer = mock<Observer<Int>>()
        viewmodel.selectedMenuItem.observeForever(observer)
        verify(observer).onChanged(R.id.menu_home)
    }

    @Test
    fun `when start application, show loading indicator`() {
        val viewmodel = MovieListViewModel(this.genreReposotiryMock, this.movieReposotiryMock)
        val result = viewmodel.loadingPanelVisibility.value
        assertEquals(View.VISIBLE, result)
    }

    @Test
    fun `when start application fetch movies`() = runBlocking {
        val genres = listOf(Genre())
        val movies = mutableListOf(Movie())
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.movies(genres)).thenReturn(movies)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        verify(observer).onChanged(movies)
    }

    @Test
    fun `when fetched movies stop show loading indicator`() = runBlocking {
        val genres = listOf(Genre())
        val movies = mutableListOf(Movie())
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.movies(genres)).thenReturn(movies)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        val loadingVisibility = viewmodel.loadingPanelVisibility.value
        assertEquals(View.GONE, loadingVisibility)
    }

    @Test
    fun `when start application fetch movies in ascending order`() = runBlocking {
        val movie1 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970"))
        val movie2 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"))
        val movie3 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980"))
        val genres = listOf(Genre())
        val movies = mutableListOf(movie1, movie2, movie3)
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.movies(genres)).thenReturn(movies)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        val result = viewmodel.movies.value
        assertEquals(movie1, result?.first())
    }

    @Test
    fun `when set sortedByDescending reorder movies`() = runBlocking {
        val movie1 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970"))
        val movie2 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"))
        val movie3 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980"))
        val genres = listOf(Genre())
        val movies = mutableListOf(movie1, movie2, movie3)
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.movies(genres)).thenReturn(movies)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        val result1 = viewmodel.movies.value
        assertEquals(movie1, result1?.first())
        viewmodel.sortByReleaseDate()
        val result2 = viewmodel.movies.value
        assertEquals(movie2, result2?.first())
    }

    @Test
    fun `when selected menu is favorites, get favorites movies`() = runBlocking {
        val genres = listOf(Genre())
        val movies = mutableListOf(Movie())
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.favorites(genres)).thenReturn(movies)
        viewmodel.selectedMenuItem.value = R.id.menu_favorites
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        verify(observer).onChanged(movies)
    }

    @Test
    fun `when request more movies and selected menu is not home and has movies to fetch do not fetch it`() = runBlocking {
        val genres = listOf(Genre())
        val movies = mutableListOf(Movie())
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.favorites(genres)).thenReturn(movies)
        viewmodel.selectedMenuItem.value = R.id.menu_favorites
        Mockito.`when`(movieReposotiryMock.canLoad).thenReturn(true)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        verify(observer).onChanged(movies)
        viewmodel.loadMoreMovies()
        verify(observer).onChanged(movies)
    }

    @Test
    fun `when request more movies and selected menu is home and has no movies to fetch do not fetch it`() = runBlocking {
        val genres = listOf(Genre())
        val movies = mutableListOf(Movie())
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.movies(genres)).thenReturn(movies)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        viewmodel.selectedMenuItem.value = R.id.menu_home
        Mockito.`when`(movieReposotiryMock.canLoad).thenReturn(false)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        verify(observer).onChanged(movies)
        viewmodel.loadMoreMovies()
        verify(observer).onChanged(movies)
    }

    @Test
    fun `when request more movies then show loading indicator`() = runBlocking {
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        viewmodel.selectedMenuItem.value = R.id.menu_home
        Mockito.`when`(movieReposotiryMock.canLoad).thenReturn(true)
        val observer = mock<Observer<Int>>()
        viewmodel.loadingPanelVisibility.observeForever(observer)
        viewmodel.loadMoreMovies()
        verify(observer).onChanged(View.VISIBLE)
    }

    @Test
    fun `when request more movies is done then stop show loading indicator `() = runBlocking {
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        viewmodel.selectedMenuItem.value = R.id.menu_home
        Mockito.`when`(movieReposotiryMock.canLoad).thenReturn(true)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        viewmodel.loadMoreMovies()
        val result = viewmodel.loadingPanelVisibility.value
        assertEquals(View.GONE, result)
    }

    @Test
    fun `when request more movies then increment current movies list`() = runBlocking {
        val movie1 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970"))
        val movie2 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"))
        val movie3 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980"))
        val movie4 = Movie(releaseDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"))
        val genres = listOf(Genre())
        val movies = mutableListOf(movie1, movie2, movie3)
        Mockito.`when`(this@MovieListViewModelTest.genreReposotiryMock.genres).thenReturn(genres)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.movies(genres)).thenReturn(movies)
        val viewmodel = MovieListViewModel(this@MovieListViewModelTest.genreReposotiryMock, this@MovieListViewModelTest.movieReposotiryMock, dispatcher = Dispatchers.Unconfined)
        val observer = mock<Observer<MutableList<Movie>?>>()
        viewmodel.movies.observeForever(observer)
        verify(observer).onChanged(movies)
        movies.add(movie4)
        Mockito.`when`(this@MovieListViewModelTest.movieReposotiryMock.movies(genres)).thenReturn(movies)
        viewmodel.loadMoreMovies()
        verify(observer).onChanged(movies)
    }
}