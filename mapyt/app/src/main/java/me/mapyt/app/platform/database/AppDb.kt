package me.mapyt.app.platform.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.mapyt.app.platform.database.sources.PlaceDao
import me.mapyt.app.platform.database.entities.SavedPlaceEntity

@Database(entities = [SavedPlaceEntity::class], version = 1)
abstract class AppDb: RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    companion object {
        const val SAVED_PLACE_TABLE = "saved_places"
        private const val DB_NAME = "places.db"

        fun create(context: Context): AppDb {
            return Room.databaseBuilder(context, AppDb::class.java, DB_NAME).build()
        }
    }
}