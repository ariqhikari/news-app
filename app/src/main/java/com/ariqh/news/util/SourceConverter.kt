package com.ariqh.news.util

import androidx.room.TypeConverter
import com.ariqh.news.source.news.SourceModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SourceConverter {
    @TypeConverter
    @JvmStatic
    fun toSource(value: String?) : SourceModel {
        val modelType = object : TypeToken<SourceModel>() {}.type
        return Gson().fromJson(value, modelType)
    }

    @TypeConverter
    @JvmStatic
    fun fromSource(source: SourceModel) : String {
        val gson = Gson()
        return gson.toJson(source)
    }

}