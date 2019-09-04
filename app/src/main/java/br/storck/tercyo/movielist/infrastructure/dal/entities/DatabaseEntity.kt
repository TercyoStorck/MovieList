package br.storck.tercyo.movielist.infrastructure.dal.entities

interface DatabaseEntity<T> {
    fun cast(): T
}