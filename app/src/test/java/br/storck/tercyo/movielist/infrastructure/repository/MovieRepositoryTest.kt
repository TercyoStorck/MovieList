package br.storck.tercyo.movielist.infrastructure.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.TheMovieEndpoints
import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.TheMovieService
import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto.MovieDTO
import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto.MovieResultsDTO
import br.storck.tercyo.movielist.infrastructure.dal.dao.MovieDAO
import br.storck.tercyo.movielist.model.Genre
import br.storck.tercyo.movielist.model.Movie
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Response

class MovieRepositoryTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val serviceMock = Mockito.mock(TheMovieService::class.java)
    val movieDaoMock = Mockito.mock(MovieDAO::class.java)

    val endpoints = Mockito.mock(TheMovieEndpoints::class.java)
    val callMock = mock<Call<MovieResultsDTO>>()
    val response = mock<Response<MovieResultsDTO>>()

    lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        this.repository = MovieRepository(serviceMock, movieDaoMock)
    }

    @Test
    fun `when fetch movies and result is null then return null`() {
        Mockito.`when`(this.serviceMock.endpoints).thenReturn(this.endpoints)
        Mockito.`when`(this.endpoints.movies(any())).thenReturn(this.callMock)
        Mockito.`when`(this.callMock.execute()).thenReturn(this.response)
        Mockito.`when`(this.response.body()).thenReturn(null)

        val result = this.repository.movies(emptyList())

        assertEquals(null, result)
    }

    @Test
    fun `when fetch movies then movies must be have associated genres`() {
        val genre1 = Genre(id = 1)
        val genre2 = Genre(id = 2)
        val genre3 = Genre(id = 3)
        val dtoMock = MovieResultsDTO(
            results  = listOf(
                MovieDTO(genreIds = listOf(1,2)),
                MovieDTO(genreIds = listOf(3)),
                MovieDTO(genreIds = listOf(1,2,3))
            )
        )

        Mockito.`when`(this.serviceMock.endpoints).thenReturn(this.endpoints)
        Mockito.`when`(this.endpoints.movies(any())).thenReturn(this.callMock)
        Mockito.`when`(this.callMock.execute()).thenReturn(this.response)
        Mockito.`when`(this.response.body()).thenReturn(dtoMock)

        val movies = this.repository.movies(listOf(genre1, genre2, genre3))!!

        assertEquals(listOf(genre1, genre2), movies[0].genres)
        assertEquals(listOf(genre3), movies[1].genres)
        assertEquals(listOf(genre1, genre2, genre3), movies[2].genres)
    }

    @Test
    fun `when fetch favorites then movies must be have associated genres`() {
        val genre1 = Genre(id = 1)
        val genre2 = Genre(id = 2)
        val genre3 = Genre(id = 3)

        val entity1 = br.storck.tercyo.movielist.infrastructure.dal.entities.Movie(1, genreIds = listOf(1,2))
        val entity2 = br.storck.tercyo.movielist.infrastructure.dal.entities.Movie(2, genreIds = listOf(3))
        val entity3 = br.storck.tercyo.movielist.infrastructure.dal.entities.Movie(2, genreIds = listOf(1,2,3))

        Mockito.`when`(movieDaoMock.getAll()).thenReturn(listOf(entity1, entity2, entity3))

        val movies = this.repository.favorites(listOf(genre1, genre2, genre3))!!

        assertEquals(listOf(genre1, genre2), movies[0].genres)
        assertEquals(listOf(genre3), movies[1].genres)
        assertEquals(listOf(genre1, genre2, genre3), movies[2].genres)
    }

    @Test
    fun `when add favorites then increment list`() {
        val movie = Movie()
        val movieEntity = br.storck.tercyo.movielist.infrastructure.dal.entities.Movie(0)
        val listMovieEntity = listOf(br.storck.tercyo.movielist.infrastructure.dal.entities.Movie(1))
        Mockito.`when`(movieDaoMock.toEntity(any())).thenReturn(movieEntity)
        Mockito.`when`(movieDaoMock.getAll()).thenReturn(listMovieEntity)
        var favorites = this.repository.favorites(listOf(Genre()))
        assertEquals(1, favorites?.size)
        this.repository.addFavorite(movie)
        favorites = this.repository.favorites(listOf(Genre()))
        assertEquals(2, favorites?.size)
    }

    @Test
    fun `when remove favorites then decrement list`() {
        val entity1 = br.storck.tercyo.movielist.infrastructure.dal.entities.Movie(1)
        val entity2 = br.storck.tercyo.movielist.infrastructure.dal.entities.Movie(2)

        Mockito.`when`(movieDaoMock.toEntity(any())).thenReturn(entity2)
        Mockito.`when`(movieDaoMock.getAll()).thenReturn(listOf(entity1, entity2))

        var favorites = this.repository.favorites(listOf(Genre()))
        assertEquals(2, favorites?.size)
        this.repository.removeFavorite(favorites?.first()!!)
        favorites = this.repository.favorites(listOf(Genre()))
        assertEquals(1, favorites?.size)
    }

    @Test
    fun `when fetch movies then check if can get more movies`() {
        val dtoMock = MovieResultsDTO(
            totalPages = 10
        )

        Mockito.`when`(this.serviceMock.endpoints).thenReturn(this.endpoints)
        Mockito.`when`(this.endpoints.movies(any())).thenReturn(this.callMock)
        Mockito.`when`(this.callMock.execute()).thenReturn(this.response)
        Mockito.`when`(this.response.body()).thenReturn(dtoMock)

        this.repository.movies(emptyList())

        assert(this.repository.canLoad)
    }
}