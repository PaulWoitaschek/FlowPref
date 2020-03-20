package de.paulwoitaschek.flowpref.android.adapter

import android.content.SharedPreferences

interface PrefAdapter<T> {

  fun get(key: String, prefs: SharedPreferences): T

  fun set(key: String, prefs: SharedPreferences, value: T, commit: Boolean = false)
}
