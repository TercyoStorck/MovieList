package br.storck.tercyo.movielist.infrastructure.resources

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask

class DownloadImage {
    fun downloadImage(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val `in` = java.net.URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }

    class Task(
        private val callback: ((result: Bitmap) -> Unit)? = null
    ): AsyncTask<String?, Void, Bitmap>() {
        override fun doInBackground(vararg urls: String?): Bitmap? {
            val urldisplay = urls[0]
            return urldisplay?.let {
                DownloadImage().downloadImage(it)
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            callback?.let { listener ->
                result?.let {
                    listener(it)
                }
            }
        }
    }
}

