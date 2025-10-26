package com.example.airsoftrockgalacticapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeDataStore(context: Context) {
    private val appContext = context.applicationContext

    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val isDarkMode: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false // Default to light mode
        }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        appContext.dataStore.edit { settings ->
            settings[IS_DARK_MODE] = isDarkMode
        }
    }
}
