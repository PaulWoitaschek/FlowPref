package de.paulwoitaschek.flowpref.android

import com.google.common.truth.Truth.assertThat
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class PrefTester<T>(private val pref: Pref<T>) {

  private val collected = mutableListOf<T>()

  private val collectJob = GlobalScope.launch(Dispatchers.Main) {
    pref.flow.collect {
      collected += it
    }
  }

  fun assert(vararg expected: T) {
    assertThat(collected).isEqualTo(expected.toList())
    assertThat(pref.value).isEqualTo(expected.last())
  }

  fun put(value: T) {
    pref.setAndCommit(value)
  }

  fun delete() {
    pref.delete()
  }

  fun release() {
    collectJob.cancel()
  }
}