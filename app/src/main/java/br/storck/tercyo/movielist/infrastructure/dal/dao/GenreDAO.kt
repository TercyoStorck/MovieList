package br.storck.tercyo.movielist.infrastructure.dal.dao

import androidx.room.*
import br.storck.tercyo.movielist.infrastructure.dal.entities.Genre

@Dao
interface GenreDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(genre: Genre)

    @Update
    fun update(genre: Genre)

    @Delete
    fun delete(genre: Genre)

    @Query("SELECT * FROM Genre")
    fun getAll(): List<Genre>?
}