package de.paulwoitaschek.flowpref.adapter

import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.FlowPref
import de.paulwoitaschek.flowpref.Pref

object StringAdapter : PrefAdapter<String> {

  override fun get(key: String, prefs: SharedPreferences): String {
    return prefs.getString(key, null)!!
  }

  override fun set(key: String, prefs: SharedPreferences, value: String, commit: Boolean) {
    prefs.edit(commit = commit) {
      putString(key, value)
    }
  }
}

fun FlowPref.string(key: String, default: String): Pref<String> {
  return create(StringAdapter, key, default)
}
