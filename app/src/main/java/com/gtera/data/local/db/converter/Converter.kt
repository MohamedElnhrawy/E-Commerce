package com.gtera.data.local.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object Converter {
    //ArrayList<String> to String
    @TypeConverter
    @JvmStatic
    fun toStringList(value: String?): ArrayList<String> {
        val listType =
            object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringList(list: ArrayList<String?>?): String {
        return Gson().toJson(list)
    }

    //Data to Long
    @TypeConverter
    @JvmStatic
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    @JvmStatic
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let { Date(it) }
    }


}