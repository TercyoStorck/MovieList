package br.storck.tercyo.movielist.model

import android.graphics.Bitmap
import java.text.SimpleDateFormat
import java.util.*



data class Movie (
    val id: Int? = null,
    val title: String? = null,
    val poster: Bitmap? = null,
    val releaseDate: Date? = null,
    var genres: List<Genre>? = null,
    val overview: String? = null,
    var favorite: Boolean? = null
) {
    val releaseDateFormatted: String
        get() {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            return this.releaseDate?.let {
                sdf.format(it)
            } ?: run {
                ""
            }
        }
}