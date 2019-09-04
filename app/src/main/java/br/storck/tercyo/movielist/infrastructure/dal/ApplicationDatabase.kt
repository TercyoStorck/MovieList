package br.storck.tercyo.movielist.infrastructure.dal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.storck.tercyo.movielist.infrastructure.dal.dao.GenreDAO
import br.storck.tercyo.movielist.infrastructure.dal.dao.MovieDAO
import br.storck.tercyo.movielist.infrastructure.dal.entities.Genre
import br.storck.tercyo.movielist.infrastructure.dal.entities.Movie

@Database(entities = arrayOf(Genre::class, Movie::class), version = 1)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase: RoomDatabase() {
    companion object {
        private var database: ApplicationDatabase? = null

        fun init(context: Context) {
            database = Room.databaseBuilder(
                context,
                ApplicationDatabase::class.java,
                "database").build()
        }

        val instance: ApplicationDatabase?
            get() = database
    }

    abstract fun genreDao(): GenreDAO
    abstract fun movieDao(): MovieDAO
}