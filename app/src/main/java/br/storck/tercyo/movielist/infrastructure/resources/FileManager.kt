package br.storck.tercyo.movielist.infrastructure.resources

import android.content.Context
import br.storck.tercyo.movielist.MovieList.Companion.application
import java.io.File
import java.io.FileInputStream
import android.graphics.Bitmap
import android.util.Log
import java.io.FileOutputStream
import android.graphics.BitmapFactory

class FileManager {
    fun saveImage(bitmap: Bitmap?, imageName: String?) {
        val foStream: FileOutputStream

        try {
            foStream = application.openFileOutput(imageName, Context.MODE_PRIVATE)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, foStream)
            foStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun loadImageBitmap(imageName: String?): Bitmap? {
        var bitmap: Bitmap? = null
        val fiStream: FileInputStream

        try {
            fiStream = application.openFileInput(imageName)
            bitmap = BitmapFactory.decodeStream(fiStream)
            fiStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }
}