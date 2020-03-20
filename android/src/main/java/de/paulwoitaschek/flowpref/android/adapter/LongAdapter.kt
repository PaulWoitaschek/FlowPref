package de.paulwoitaschek.flowpref.android.adapter

import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.android.FlowPref

internal object LongAdapter : PrefAdapter<Long> {

  override fun get(key: String, prefs: SharedPreferences): Long {
    return prefs.getLong(key, 0)
  }

  override fun set(key: String, prefs: SharedPreferences, value: Long, commit: Boolean) {
    prefs.edit(commit = commit) { putLong(key, value) }
  }
}

@Suppress("unused")
fun FlowPref.long(key: String, default: Long): Pref<Long> {
  return create(LongAdapter, key, default)
}
