package de.paulwoitaschek.flowpref.android

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

private const val DEFAULT_KEY = "key"


@OptIn(ExperimentalCoroutinesApi::class)
class AndroidPreferencesTest {

  private val prefs: AndroidPreferences
  private val sharedPrefs: SharedPreferences

  init {
    val context = ApplicationProvider.getApplicationContext<Context>()
    sharedPrefs = context.getSharedPreferences("testPrefs", Context.MODE_PRIVATE)
    prefs = AndroidPreferences(sharedPrefs)
  }

  @Test
  fun clearWithCommit() {
    val pref = prefs.string(DEFAULT_KEY, "Bob")
    pref.setAndCommit("Alice")
    prefs.clear(commit = true)
    assertSharedPrefValue(null)
    assertThat(pref.value).isEqualTo("Bob")
  }

  @Test
  fun clearWithoutCommit() {
    val pref = prefs.string(DEFAULT_KEY, "Bob")
    pref.setAndCommit("Alice")
    prefs.clear(commit = true)
    assertSharedPrefValue(null)
    assertThat(pref.value).isEqualTo("Bob")
  }


  @Test
  fun flow() = runBlockingTest {
    val pref = prefs.string(DEFAULT_KEY, "Bob")
    var collected = emptyList<String>()
    val job = launch {
      pref.flow.collect {
        collected = collected + it
      }
    }

    fun assertCollectedAndClear(vararg values: String) {
      assertThat(collected).isEqualTo(values.toList())
      collected = emptyList()
    }

    assertCollectedAndClear("Bob")

    pref.value = "1"
    assertCollectedAndClear("1")

    pref.setAndCommit("2")
    assertCollectedAndClear("2")

    pref.delete(commit = true)
    assertCollectedAndClear("Bob")

    pref.setAndCommit("John")
    pref.delete(commit = false)
    assertCollectedAndClear("John", "Bob")

    pref.value = "Alice"
    prefs.clear(commit = false)
    assertCollectedAndClear("Alice", "Bob")

    job.cancel()
  }


  @Test
  fun setWithCommit() {
    val pref = prefs.string(DEFAULT_KEY, "Bob")

    assertSharedPrefValue(null)
    pref.assertValue("Bob")

    pref.setAndCommit("Alice")
    pref.assertValue("Alice")
    assertSharedPrefValue("Alice")

    pref.setAndCommit("John")
    pref.assertValue("John")
    assertSharedPrefValue("John")
  }

  @Test
  fun setWithoutCommit() {
    val pref = prefs.string(DEFAULT_KEY, "Bob")

    assertSharedPrefValue(null)
    pref.assertValue("Bob")

    pref.value = "Alice"
    pref.assertValue("Alice")
    assertSharedPrefValue("Alice")

    pref.value = "John"
    pref.assertValue("John")
    assertSharedPrefValue("John")
  }

  @Test
  fun deleteWithCommit() {
    val pref = prefs.string(DEFAULT_KEY, "Bob")
    assertSharedPrefValue(null)
    pref.setAndCommit("Alice")
    pref.delete(commit = true)
    pref.assertValue("Bob")
    assertSharedPrefValue(null)
  }

  @Test
  fun deleteWithoutCommit() {
    val pref = prefs.string(DEFAULT_KEY, "Bob")
    assertSharedPrefValue(null)
    pref.setAndCommit("Alice")
    pref.delete(commit = false)
    pref.assertValue("Bob")
    assertSharedPrefValue(null)
  }

  private fun <T> Pref<T>.assertValue(value: T?) {
    assertThat(this.value).isEqualTo(value)
  }

  private fun assertSharedPrefValue(value: String?) {
    assertThat(sharedPrefs.getString(DEFAULT_KEY, null)).isEqualTo(value)
  }

  @Test
  fun onlyChangedFlowTriggers() = runBlockingTest {
    val prefA = prefs.int("preA", 42)
    val prefB = prefs.int("prefB", 42)

    var collectedA = listOf<Int>()
    var collectedB = listOf<Int>()

    val jobA = launch {
      prefA.flow.collect { collectedA = collectedA + it }
    }
    val jobB = launch {
      prefB.flow.collect { collectedB = collectedB + it }
    }

    fun assert(a: List<Int>, b: List<Int>) {
      assertThat(collectedA).isEqualTo(a)
      assertThat(collectedB).isEqualTo(b)
    }

    prefA.setAndCommit(1)
    assert(
      a = listOf(42, 1),
      b = listOf(42)
    )

    prefA.setAndCommit(2)
    assert(
      a = listOf(42, 1, 2),
      b = listOf(42)
    )

    prefB.setAndCommit(1)
    assert(
      a = listOf(42, 1, 2),
      b = listOf(42, 1)
    )

    jobA.cancel()
    jobB.cancel()
  }
}
