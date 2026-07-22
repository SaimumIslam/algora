package com.algora.app.core.data.progress

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context

val Context.progressDataStore: DataStore<Preferences> by preferencesDataStore(name = "algora_progress")

object ProgressKeys {
    val COMPLETED_TOPIC_IDS = stringSetPreferencesKey("completed_topic_ids")
}
