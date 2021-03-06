package me.mapyt.app

import android.content.Context
import me.mapyt.app.platform.database.AppDb

//TODO: reemplazar por Component de Koin
class DbContainer(appContext: Context) {
    //TODO: verificar que no se cree multiples veces
    val appDb = AppDb.create(appContext)
}