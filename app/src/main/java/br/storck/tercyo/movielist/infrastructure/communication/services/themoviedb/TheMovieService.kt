package br.storck.tercyo.movielist.infrastructure.communication.services.themoviedb

import br.storck.tercyo.movielist.infrastructure.communication.services.ServiceBase

class TheMovieService(
    dateFormat: String? = null
): ServiceBase<TheMovieEndpoints>(
    baseURL = "https://api.themoviedb.org",
    dateFormat = dateFormat,
    clazz = TheMovieEndpoints::class.java
)