package de.paulwoitaschek.flowpref.android.adapter

import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.android.FlowPref

internal object BooleanAdapter : PrefAdapter<Boolean> {

  override fun get(key: String, prefs: SharedPreferences): Boolean {
    return prefs.getBoolean(key, false)
  }

  override fun set(key: String, prefs: SharedPreferences, value: Boolean, commit: Boolean) {
    prefs.edit(commit = commit) { putBoolean(key, value) }
  }
}

@Suppress("unused")
fun FlowPref.boolean(key: String, default: Boolean): Pref<Boolean> {
  return create(BooleanAdapter, key, default)
}
