package de.paulwoitaschek.flowpref.android.internal

import android.content.SharedPreferences
import androidx.core.content.edit
import de.paulwoitaschek.flowpref.android.adapter.PrefAdapter

internal class DelegatingPrefAdapter<T>(private val delegate: PrefAdapter<T>) :
  InternalPrefAdapter<T> {

  override fun get(key: String, prefs: SharedPreferences): T {
    val stringValue = prefs.getString(key, null)
    return delegate.parse(stringValue)
  }

  override fun set(key: String, prefs: SharedPreferences, value: T, commit: Boolean) {
    val stringValue = delegate.serialize(value)
    prefs.edit(commit = commit) {
      putString(key, stringValue)
    }
  }
}
