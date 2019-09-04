package br.storck.tercyo.movielist

import android.app.Application
import android.content.Context
import br.storck.tercyo.movielist.infrastructure.dal.ApplicationDatabase

class MovieList: Application() {
    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        ApplicationDatabase.init(this)
    }
}