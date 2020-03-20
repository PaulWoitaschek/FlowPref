package de.paulwoitaschek.flowpref.android

import android.content.SharedPreferences
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.android.adapter.PrefAdapter
import de.paulwoitaschek.flowpref.android.internal.RealPref
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class FlowPref(private val sharedPrefs: SharedPreferences) {

  private val registered = mutableListOf<RealPref<*>>()

  // because the shared preferences use a week reference when registering, we need to keep a reference
  private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
    registered.forEach { pref ->
      if (pref.key == key) {
        pref.notifyChanged()
      }
    }
  }

  init {
    sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
  }

  fun <T> create(adapter: PrefAdapter<T>, key: String, default: T): Pref<T> {
    return RealPref(
      sharedPrefs,
      adapter,
      key,
      default
    ).also {
      registered += it
    }
  }
}
