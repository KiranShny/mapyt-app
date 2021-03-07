package me.mapyt.app.platform.database.sources

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import me.mapyt.app.core.data.PlacesLocalSource
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.platform.database.entities.toDomainDetails
import me.mapyt.app.platform.database.entities.toSavedPlaceEntity

class PlacesLocalSourceImpl(
    private val dao: PlaceDao,
    private val opsDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PlacesLocalSource {

    override suspend fun getAll(): List<PlaceDetails> {
        return withContext(opsDispatcher) {
            val dbResponse = dao.getAll()
            dbResponse.map { it.toDomainDetails() }
        }
    }

    override suspend fun get(code: String): PlaceDetails {
        return withContext(opsDispatcher) {
            val dbResponse = dao.get(code)
            dbResponse.toDomainDetails()
        }
    }

    override suspend fun exists(code: String): Boolean {
        return withContext(opsDispatcher) {
            delay(500) //just for testing
            dao.exists(code)
        }
    }

    override suspend fun insert(entity: PlaceDetails) {
        return withContext(opsDispatcher) {
            dao.insert(entity.toSavedPlaceEntity())
        }
    }

    override suspend fun delete(entity: PlaceDetails) {
        return withContext(opsDispatcher) {
            dao.delete(entity.toSavedPlaceEntity())
        }
    }

    override suspend fun deleteAll() {
        return withContext(opsDispatcher) {
            dao.deleteAll()
        }
    }
}