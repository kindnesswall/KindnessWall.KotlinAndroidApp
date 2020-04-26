package ir.kindnesswall.utils.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class DataConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date {
        return if (timestamp == null) Date() else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long {
        return (date?.time) ?: Date().time
    }

    @TypeConverter
    fun toImageList(data: String?): List<String> {
        if (data == null) {
            return emptyList()
        }

        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun fromImagesList(data: List<String>?): String {
        if (data == null) {
            return ""
        }

        val type = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(data, type)
    }
}