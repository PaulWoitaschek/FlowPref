package de.paulwoitaschek.flowpref.android.internal.adapter

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import de.paulwoitaschek.flowpref.android.internal.InternalPrefAdapter

internal class AdapterTester<T>(private val adapter: InternalPrefAdapter<T>) {

  private val key = System.nanoTime().toString()

  private val prefs: SharedPreferences = ApplicationProvider.getApplicationContext<Context>()
    .getSharedPreferences(key, Context.MODE_PRIVATE)

  fun test(value: T) {
    adapter.set(key, prefs, value)
    assertThat(adapter.get(key, prefs)).isEqualTo(value)
  }
}
