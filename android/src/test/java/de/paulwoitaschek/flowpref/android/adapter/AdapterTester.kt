package de.paulwoitaschek.flowpref.android.adapter

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat

class AdapterTester<T>(private val adapter: PrefAdapter<T>) {

  private val key = System.nanoTime().toString()

  private val prefs: SharedPreferences = ApplicationProvider.getApplicationContext<Context>()
    .getSharedPreferences(key, Context.MODE_PRIVATE)

  fun test(value: T) {
    adapter.set(key, prefs, value)
    assertThat(adapter.get(key, prefs)).isEqualTo(value)
  }
}
