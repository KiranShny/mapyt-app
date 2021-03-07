package me.mapyt.app.platform.database.sources

import androidx.room.*
import me.mapyt.app.platform.database.AppDb
import me.mapyt.app.platform.database.entities.SavedPlaceEntity

@Dao
interface PlaceDao {
    @Query("SELECT * FROM ${AppDb.SAVED_PLACE_TABLE}")
    suspend fun getAll(): List<SavedPlaceEntity>

    @Query("SELECT * FROM ${AppDb.SAVED_PLACE_TABLE} WHERE id = :code")
    suspend fun get(code: String): SavedPlaceEntity

    @Query("SELECT EXISTS(SELECT 1 FROM ${AppDb.SAVED_PLACE_TABLE} WHERE id = :code LIMIT 1)")
    suspend fun exists(code: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedPlaceEntity)

    @Delete
    suspend fun delete(entity: SavedPlaceEntity)

    @Query("DELETE FROM ${AppDb.SAVED_PLACE_TABLE}")
    suspend fun deleteAll()
}