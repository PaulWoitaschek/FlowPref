package de.paulwoitaschek.flowpref.android.adapter

import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.android.FlowPref

object IntAdapter : PrefAdapter<Int> {

  override fun get(key: String, prefs: SharedPreferences): Int {
    return prefs.getInt(key, 0)
  }

  override fun set(key: String, prefs: SharedPreferences, value: Int, commit: Boolean) {
    prefs.edit(commit = commit) { putInt(key, value) }
  }
}

@Suppress("unused")
fun FlowPref.int(key: String, default: Int): Pref<Int> {
  return create(IntAdapter, key, default)
}
