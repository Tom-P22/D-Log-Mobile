package com.duoc.d_log_mobile.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

// Una sola instancia del archivo de preferencias para toda la app.
private val Context.preferencias by preferencesDataStore(name = "disena_madera")

class PreferenciasRepository(context: Context) {
    private val dataStore = context.applicationContext.preferencias
    private val sesionKey = booleanPreferencesKey("sesion_iniciada")
    private val favoritosKey = stringSetPreferencesKey("favoritos")
    private val datos = dataStore.data.catch { error ->
        if (error is IOException) emit(emptyPreferences()) else throw error
    }

    val sesionIniciada = datos.map { it[sesionKey] ?: false }
    val favoritos = datos.map { it[favoritosKey] ?: emptySet() }

    suspend fun guardarSesion(iniciada: Boolean) {
        dataStore.edit { it[sesionKey] = iniciada }
    }

    suspend fun guardarFavoritos(ids: Set<String>) {
        dataStore.edit { it[favoritosKey] = ids }
    }
}
