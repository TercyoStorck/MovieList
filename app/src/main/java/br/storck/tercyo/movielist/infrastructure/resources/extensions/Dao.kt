package br.storck.tercyo.movielist.infrastructure.resources.extensions

import br.storck.tercyo.movielist.infrastructure.dal.entities.DatabaseEntity

fun <T> List<DatabaseEntity<T>>.cast(): List<T> {
    return this.map { entity ->
        entity.cast()
    }
}