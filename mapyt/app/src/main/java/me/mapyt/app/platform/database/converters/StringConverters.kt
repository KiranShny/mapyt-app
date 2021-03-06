package me.mapyt.app.platform.database.converters

import androidx.room.TypeConverter

class StringListConverter {
    companion object {
        const val DELIMITER = ","
    }

    @TypeConverter
    fun fromString(listString: String): List<String> = listString.split(DELIMITER).map { it }

    @TypeConverter
    fun toString(stringList: List<String>): String = stringList.joinToString(DELIMITER)
}