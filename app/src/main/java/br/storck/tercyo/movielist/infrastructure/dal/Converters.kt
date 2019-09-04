package br.storck.tercyo.movielist.infrastructure.dal

import androidx.room.TypeConverter
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromstringToListInt(value: String): List<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson<List<Int>>(value, listType)
    }

    @TypeConverter
    fun fromListToJson(list: List<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}