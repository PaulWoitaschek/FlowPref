package de.paulwoitaschek.flowpref.android

import android.content.SharedPreferences
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.android.internal.DelegatingPrefAdapter
import de.paulwoitaschek.flowpref.android.internal.InternalPrefAdapter
import de.paulwoitaschek.flowpref.android.internal.RealPref
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.CopyOnWriteArrayList

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class PreferenceFactory(private val sharedPrefs: SharedPreferences) {

  private val registered = CopyOnWriteArrayList<RealPref<*>>()

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

  @Suppress("unused")
  fun <T> create(key: String, default: T, adapter: PrefAdapter<T>): Pref<T> {
    return create(key, default, DelegatingPrefAdapter(adapter))
  }

  internal fun <T> create(
    key: String,
    default: T,
    adapter: InternalPrefAdapter<T>
  ): Pref<T> {
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
