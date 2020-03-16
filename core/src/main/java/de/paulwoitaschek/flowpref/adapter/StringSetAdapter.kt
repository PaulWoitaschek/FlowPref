package de.paulwoitaschek.flowpref.adapter

import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.FlowPref
import de.paulwoitaschek.flowpref.Pref

object StringSetAdapter : PrefAdapter<Set<String>> {

  override fun get(key: String, prefs: SharedPreferences): Set<String> {
    return prefs.getStringSet(key, emptySet())!!
  }

  override fun set(key: String, prefs: SharedPreferences, value: Set<String>, commit: Boolean) {
    prefs.edit(commit = commit) { putStringSet(key, value) }
  }
}


@Suppress("unused")
fun FlowPref.stringSet(key: String, default: Set<String>): Pref<Set<String>> {
  return create(StringSetAdapter, key, default)
}
