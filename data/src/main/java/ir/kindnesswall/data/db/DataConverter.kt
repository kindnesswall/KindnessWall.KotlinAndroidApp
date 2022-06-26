package ir.kindnesswall.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.util.*

class DataConverter {
  @TypeConverter
  fun toBigDecimal(string: String?): BigDecimal {
    return if (string.isNullOrEmpty()) BigDecimal.ZERO else string.toBigDecimal()
  }

  @TypeConverter
  fun fromBigDecimal(bigDecimal: BigDecimal): String {
    return bigDecimal.toString()
  }

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

  @TypeConverter
  fun toImageArrayList(data: String?): ArrayList<String> {
    if (data == null) {
      return arrayListOf()
    }

    val type = object : TypeToken<ArrayList<String>>() {}.type
    return Gson().fromJson(data, type)
  }

  @TypeConverter
  fun fromImagesArrayList(data: ArrayList<String>?): String {
    if (data == null) {
      return ""
    }

    val type = object : TypeToken<ArrayList<String>>() {}.type
    return Gson().toJson(data, type)
  }
}