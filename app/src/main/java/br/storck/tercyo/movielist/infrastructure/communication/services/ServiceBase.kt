package br.storck.tercyo.movielist.infrastructure.communication.services

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.concurrent.TimeUnit


open class ServiceBase<T>(
    val baseURL: String,
    var dateFormat: String?,
    val clazz: Class<T>
) {
    private val gsonConverterFactory: GsonConverterFactory
    private val provideOkHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
        }

    init {
        val gsonBuilder = if (this.dateFormat?.isNotEmpty() == true) GsonBuilder().setDateFormat(this.dateFormat)?.create() else Gson()
        this.gsonConverterFactory = GsonConverterFactory.create(gsonBuilder ?: Gson())
    }

    val endpoints: T
        get() = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(this.gsonConverterFactory)
            .client(this.provideOkHttpClient)
            .build()
            .create(clazz)


}