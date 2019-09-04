package br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb

import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto.GenreDTO
import br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb.dto.MovieResultsDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieEndpoints {
    @GET(value = "/3/genre/movie/list?api_key=1f54bd990f1cdfb230adb312546d765d&language=pt-BR")
    fun genre(): Call<GenreDTO>

    @GET(value = "/3/movie/upcoming?api_key=1f54bd990f1cdfb230adb312546d765d&language=pt-BR")
    fun movies(@Query("page") address: Int): Call<MovieResultsDTO>
}