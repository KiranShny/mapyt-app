package me.mapyt.app.platform.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.mapyt.app.platform.database.converters.StringListConverter

@Entity(tableName = "saved_places")
@TypeConverters(StringListConverter::class)
data class SavedPlaceEntity(
    @PrimaryKey @ColumnInfo(name = "id") val placeId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lng")
    val lng: Double,
    @ColumnInfo(name = "address")
    val address: String = "",
    @ColumnInfo(name = "rating")
    val rating: Double = 0.0,
    @ColumnInfo(name = "user_ratings_total")
    val userRatingsTotal: Double = 0.0,
    @ColumnInfo(name = "photo_refs")
    val photosRefs: List<String> = emptyList(),
    //TODO: reviews
)