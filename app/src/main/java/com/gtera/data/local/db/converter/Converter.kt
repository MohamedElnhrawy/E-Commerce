package com.gtera.data.local.db.converter

import android.text.TextUtils
import androidx.room.TypeConverter
import com.gtera.data.model.City
import com.gtera.data.model.Governorate
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

    @TypeConverter
    @JvmStatic
    fun fromGovernorate(governorate: Governorate?): String? {
        if (governorate == null) return null
        val gson = Gson()
        val type = object : TypeToken<Governorate?>() {}.type
        return gson.toJson(governorate, type)
    }

    @TypeConverter
    @JvmStatic
    fun toGovernorate(governorateString: String?): Governorate? {
        if (TextUtils.isEmpty(governorateString)) return null
        val gson = Gson()
        val type = object : TypeToken<Governorate?>() {}.type
        return gson.fromJson(governorateString, type)
    }


    @TypeConverter
    @JvmStatic
    fun fromCity(city: City?): String? {
        if (city == null) return null
        val gson = Gson()
        val type = object : TypeToken<City?>() {}.type
        return gson.toJson(city, type)
    }

    @TypeConverter
    @JvmStatic
    fun toCity(cityString: String?): City? {
        if (TextUtils.isEmpty(cityString)) return null
        val gson = Gson()
        val type = object : TypeToken<City?>() {}.type
        return gson.fromJson(cityString, type)
    }
}